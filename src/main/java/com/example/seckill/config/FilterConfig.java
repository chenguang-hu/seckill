package com.example.seckill.config;

import com.example.seckill.filter.SSOFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private SSOFilter ssoFilter;

    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(ssoFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("excludes", "^/((homepage$)|(sec_goods$)|(getSecGoodsList$)|(.*\\.png)|(.*\\.ico))");
        registrationBean.addInitParameter("serverBaseUrl", "https://www.sso.com:8082");
        registrationBean.addInitParameter("serverInnerAddress", "https://127.0.0.1:8082");
        registrationBean.addInitParameter("notLoginOnFail", "false");
        registrationBean.setName("ssoFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
