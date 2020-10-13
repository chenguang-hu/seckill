package com.example.seckill.filter;

import com.example.seckill.entity.User;

import javax.servlet.ServletRequest;

public class UserHolder {

    private static final String REQ_USER_ATTR_NAME = "__current_sso_user";

    // 将当前登录信息放在ThreadLocal中
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    private UserHolder() {

    }

    /**
     * 获取User实例, 此方法从ThreadLocal中获取, 开辟新线程无法取到ThreadLocal中的值
     * @return
     */
    public static User getUser() {
        return userThreadLocal.get();
    }

    /**
     * 从当前请求的属性中获取User
     * @param request
     * @return
     */
    public static User getUser(ServletRequest request){
        return (User) request.getAttribute(REQ_USER_ATTR_NAME);
    }

    /**
     * 用户加入到request和ThreadLocal中供业务系统使用
     * 以default为方法的作用范围, 仅本包内代码可以访问, 对用户代码隐藏
     * @param user
     * @param request
     */
    static void set(User user, ServletRequest request){
        request.setAttribute(REQ_USER_ATTR_NAME, user);
        userThreadLocal.set(user);
    }
}
