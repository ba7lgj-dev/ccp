package org.ba7lgj.ccp.miniprogram.config;

import org.ba7lgj.ccp.miniprogram.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MpWebMvcConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    public MpWebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/mp/**", "/miniprogram/**")
                .excludePathPatterns(
                        "/login",
                        "/ruoyi/login",
                        "/admin/login",
                        "/captcha",
                        "/captchaImage",
                        "/swagger-ui/**",
                        "/druid/**",
                        "/mp/login",
                        "/mp/school/list",
                        "/mp/campus/listBySchool",
                        "/mp/gate/listByCampus"
                );
    }
}
