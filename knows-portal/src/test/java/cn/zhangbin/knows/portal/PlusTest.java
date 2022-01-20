package cn.zhangbin.knows.portal;

import cn.zhangbin.knows.portal.mapper.TagMapper;
import cn.zhangbin.knows.portal.mapper.UserMapper;
import cn.zhangbin.knows.portal.model.Permission;
import cn.zhangbin.knows.portal.model.Role;
import cn.zhangbin.knows.portal.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class PlusTest {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    UserMapper userMapper;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void getPermission(){
        List<Permission> list = userMapper.findUserPermissionById(3);
        for (Permission p : list) {
            System.out.println(p);
        }
    }

    @Test
    public void encode(){
        //encoder.encode就是加密方法,返回加密后的结果
        String pwd = encoder.encode("123456");
        System.out.println(pwd);
        // 测试运行之后会得到下面的加密结果
        // 每次加密结果不同是为了保证安全性
        // 这种每次生成加密结果不同的现象称:随机加盐技术
        //$2a$10$uGdbi8FuB5Z5z4WM3Z8GY.UoPCVEL840LurYnakMgrGMXWRmL9nt.
    }

    @Test
    public void match(){
        //验证字符串是否匹配
        boolean b = encoder.matches("123456","$2a$10$uGdbi8FuB5Z5z4WM3Z8GY.UoPCVEL840LurYnakMgrGMXWRmL9nt.");
        System.out.println(b);
    }

    @Test
    public void addTest(){
        Tag tag = new Tag();
        tag.setId(21);
        tag.setName("微服务");
        tag.setCreateby("admin");
        //tag.setCreatetime("2021/07/23 11:53:00");

        tagMapper.insert(tag);
        System.out.println("新增完成");
    }

    @Test
    public void selectOne(){
        Tag tag = tagMapper.selectById(10);
        System.out.println(tag);
    }

    @Test
    public void selectAll(){
        List<Tag> tags = tagMapper.selectList(null);
        tags.forEach(tag -> System.out.println(tag));
    }

    @Test
    public void updateOne(){
        Tag tag = tagMapper.selectById(10);
        String format = "yyyy/MM/dd HH:mm:ss";
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String dateTime = formatter.format(date);
        tag.setCreatetime(date);
        tagMapper.updateById(tag);
//        System.out.println(tagMapper.selectById(10));
        log.debug("修改完成");
    }

    @Test
    public void findUserRole(){
        List<Role> list = userMapper.findUserRolesById(12);
        list.forEach(role -> System.out.println(role));
    }
}
