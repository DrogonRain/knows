package cn.zhangbin.knows.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.zhangbin.knows.sys.mapper")
public class KnowsSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsSysApplication.class, args);
    }

    //添加Ribbon的支持,便于后面auth模块解析jwt令牌
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
