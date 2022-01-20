package cn.zhangbin.knows.faq.security;

import cn.zhangbin.knows.faq.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置跨域配置
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedOrigins("*");
        //如果实际开发中需要指定网关或前段项目能访问
        //就在allowedOrigins方法中指定允许的url就可以了
    }

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/v2/questions",
                        "/v2/questions/my",
                        "/v2/questions/teacher",
                        "/v2/questions/hot",
                        "/v2/answers",
                        "/v2/comments",
                        "/v2/comments/*/delete",
                        "/v2/comments/*/update",
                        "/v2/answers/*/solved");
    }
}
