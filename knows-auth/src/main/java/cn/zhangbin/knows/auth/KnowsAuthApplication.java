package cn.zhangbin.knows.auth;

import cn.zhangbin.knows.auth.filter.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class KnowsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsAuthApplication.class, args);
    }

    //Auth授权服务器需要Ribbon支持
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        //开始注册过滤器到SpringBoot
        bean.addUrlPatterns("/*");//设置什么样的路径需要过滤
        bean.setFilter(new CorsFilter());//实例化过滤器对象
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);//设置过滤器优先级
        return bean;
    }

}
