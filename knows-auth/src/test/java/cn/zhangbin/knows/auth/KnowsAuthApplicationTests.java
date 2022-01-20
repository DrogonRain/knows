package cn.zhangbin.knows.auth;

import cn.zhangbin.knows.auth.service.UserDetailsServiceImpl;
import cn.zhangbin.knows.commons.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;

@SpringBootTest
class KnowsAuthApplicationTests {

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Test
    void contextLoads() {
        UserDetails user = userDetailsService.loadUserByUsername("st2");
        System.out.println(user);
    }

}
