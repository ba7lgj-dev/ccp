package org.ba7lgj.ccp.miniprogram.config;

import org.ba7lgj.ccp.miniprogram.interceptor.MpAuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MpWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private MpAuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/mp/**", "/miniprogram/**")
                .excludePathPatterns(
                        "/login",
                        "/ruoyi/login",
                        "/admin/login",
                        "/captcha",
                        "/captchaImage",
                        "/swagger-ui/**",
                        "/druid/**",
                        "/mp/auth/wxLogin",
                        "/mp/auth/wxPhoneBind",
                        "/mp/school/list",
                        "/mp/campus/listBySchool",
                        "/mp/gate/listByCampus"
                );
    }
}
