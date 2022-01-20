package cn.zhangbin.knows.sys;

import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class KnowsSysApplicationTests {

    @Resource
    UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void getUser(){
        User user = userMapper.findUserByUsername("st2");
        System.out.println(user);
    }

}
