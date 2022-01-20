package cn.zhangbin.knows.faq.service.impl;

import cn.zhangbin.knows.commons.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RibbonClient {
    @Autowired
    private RestTemplate restTemplate;

    //根据用户名获取用户信息
    public User getUser(String username){
        String url = "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,username);
        return user;
    }
}
