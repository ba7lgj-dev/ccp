package org.ba7lgj.ccp.miniprogram.config;

import org.ba7lgj.ccp.miniprogram.interceptor.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/wxLogin",
                        "/auth/wxPhoneBind",
                        "/mp/school/list",
                        "/mp/campus/listBySchool",
                        "/mp/gate/listByCampus"
                );
    }
}
