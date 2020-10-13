package com.example.seckill.filter;

import com.example.seckill.entity.User;
import com.example.seckill.util.CookieUtil;
import com.example.seckill.util.TokenManager;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;


/**
 * 用户登录状态拦截验证
 */
@Component
public class SSOFilter implements Filter {

    private String excludes;    // 不需要拦截的url
    private String serverBaseUrl;   // 服务器访问地址
    private String serverInnerAddress;  // 服务器系统间通信的内网地址

    private boolean notLoginOnFail;     // 当授权失败时是否让浏览器跳转到服务端登录页

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludes = filterConfig.getInitParameter("excludes");
        serverBaseUrl = filterConfig.getInitParameter("serverBaseUrl");
        serverInnerAddress = filterConfig.getInitParameter("serverInnerAddress");

        notLoginOnFail = Boolean.parseBoolean(filterConfig.getInitParameter("notLoginOnFail"));

        if (serverBaseUrl == null || serverInnerAddress == null) {
            throw new ServletException("SSOFilter配置错误, 缺少参数");
        }

        TokenManager.serverInnerAddress = serverInnerAddress;

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 如果是不需要拦截的请求直接跳过
        if (requestIsExclude(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 进行登录状态验证
        String vt = CookieUtil.getCookie("VT", request);

        if (vt != null) {       // vt存在
            User user = null;

            try {
                user = TokenManager.validate(vt);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (user != null) {
                holdUser(user, request);     // 通过ThreadLocal将user存放, 供业务系统使用
                filterChain.doFilter(request, response);    // 请求继续向下执行
            } else {

                // 删除无效的VT cookie
                CookieUtil.deleteCookie("VT", response, "/");

                // 引导浏览器重定向到服务器执行登录校验
                loginCheck(request, response);
            }
        } else {    // vt为空

            // 从请求中获取vt
            String vtParam = parseVtParam(request);

            if (vtParam == null) {

                // 请求中没有vtParam, 引导浏览器重定向到服务器执行登录校验
                loginCheck(request, response);
            } else if (vtParam.length() == 0) {

                // 有vtParam, 但内容为空, 表示到服务端loginCheck后, 得到的结果是未登录
                response.sendError(403);
            } else {

                // 让浏览器向本连接发起一次重定向, 此过程去除vtParam, 将vt写入cookie
                redirectToSelf(vtParam, request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private boolean requestIsExclude(ServletRequest request) {

        // 没有设定excludes时，所有经过filter的请求都需要处理
        if (excludes == null) {
            return false;
        }

        // 获取去除context path后的请求路径
        String contextPath = request.getServletContext().getContextPath();
        String uri = ((HttpServletRequest) request).getRequestURI();
        uri = uri.substring(contextPath.length());

        // 不需要拦截
        boolean isExcluded = uri.matches(excludes);

        if (isExcluded) {
            System.err.println("request path is excluded");
        }

        return isExcluded;
    }

    // 将user存放在ThreadLocal与request中, 供业务系统使用
    private void holdUser(User user, ServletRequest request) {
        UserHolder.set(user, request);
    }


    private void loginCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ajax类型请求涉及跨域问题
        // CORS方案解决跨域操作时, 无法携带Cookie, 无法完成认证, 不合适
        // Jsonp方案可以处理Cookie问题, 但会对后台代码有影响, 能实现但复杂且不理想
        // Ajax请求建议先让业务系统获取到vt, 这样发起Ajax请求时就不会执行跳转认证操作
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(400);
        } else {
            /*
            redirect只能是get请求, 如果当前为post请求, 会将post过来的请求参数变成url
            这种情况下, 此处实现由一个局限性, 请求参数长度限制, 如果post过来的内容过大, 会造成参数丢失
             */

            // 拼接请求
            String qstr = makeQueryString(request);

            // 回调Url
            String backUrl = request.getRequestURL() + qstr;

            String location = serverBaseUrl + "/login?backUrl=" + URLEncoder.encode(backUrl, "utf-8");

            // 普通请求类型
            if (!notLoginOnFail) {
                location += "&notLogin=" + notLoginOnFail;
            }
            response.sendRedirect(location);
        }
    }

    // 拼接请求
    private String makeQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paraName = parameterNames.nextElement();

            // 同名参数取数组  a=1&a=2
            String[] paraVals = request.getParameterValues(paraName);

            // 得到字符串    &a=1&a=2&b=xxx
            for (String paraVal : paraVals) {
                builder.append("&").append(paraName).append("=").append(URLEncoder.encode(paraVal, "utf-8"));
            }
        }

        // 将上面得到的字符串的第一个&替换成？
        if (builder.length() > 0) {
            builder.replace(0, 1, "?");
        }

        return builder.toString();
    }


    // 从请求中解析vt
    private String parseVtParam(HttpServletRequest request) {

        // 例如：http://www.aaa.com:8082/test/tt?a=2&b=xx&__vt_param__=xxxx

        final String PARANAME = "__vt_param__=";

        String qstr = request.getQueryString();

        // 当前的请求没有任何参数
        if (qstr == null) {
            return null;
        }

        int index = qstr.indexOf(PARANAME);
        if (index > -1) {
            return qstr.substring(index + PARANAME.length());
        } else {
            return null;
        }
    }


    // 从参数中获取服务端传来的vt后, 执行一个到本链接的重定向, 将vt写入cookie
    private void redirectToSelf(String vt, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 例如：http://www.aaa.com:8082/test/tt?a=2&b=xx&__vt_param__=xxxx

        final String PARANAME = "__vt_param__=";

        // 此处拼接redirect的url, 去除vt参数部分
        // 得到http://www.aaa.com:8082/test/tt
        StringBuffer location = request.getRequestURL();

        String qstr = request.getQueryString();
        int index = qstr.indexOf(PARANAME);

        if (index > 0) {
            qstr = "?" + qstr.substring(0, qstr.indexOf(PARANAME) - 1);
        } else {
            qstr = "";
        }

        location.append(qstr);

        Cookie cookie = new Cookie("VT", vt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        response.sendRedirect(location.toString());
    }
}
