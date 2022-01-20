package cn.zhangbin.knows.auth.service;

import cn.zhangbin.knows.commons.model.Permission;
import cn.zhangbin.knows.commons.model.Role;
import cn.zhangbin.knows.commons.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.设置请求地址(url地址)
        String url = "http://sys-service/v1/auth/user?username={1}";
        //2.根据用户名查询用户信息
        User user = restTemplate.getForObject(url, User.class,username);
        //3.如果用户不存在则抛出异常
        if (user==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        //4.设置permission的请求地址
        url = "http://sys-service/v1/auth/permissions?id={1}";
        //5.Ribbon调用返回值是List类型,则接受返回值的类型是泛型类型的数组
        Permission[] permissions = restTemplate.getForObject(url, Permission[].class,user.getId());
        //6.设置roles的请求地址
        url = "http://sys-service/v1/auth/roles?id={1}";
        //7.Ribbon调用查询所有roles
        Role[] roles = restTemplate.getForObject(url,Role[].class,user.getId());
        //8.实例化auth数组,数组长度为permission与roles之和
        String[] auth = new String[permissions.length+roles.length];
        //9.遍历所有permission与role并将名称添加至auth数组
        int i=0;
        for (Permission permission : permissions){
            auth[i++] = permission.getName();
        }
        for (Role role : roles){
            auth[i++] = role.getName();
        }
        //10.构建UserDetails对象并返回
        UserDetails u = org.springframework.security.core.userdetails
                .User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)
                .disabled(user.getEnabled()==0)
                .build();
        //11.返回UserDetails
        return u;
    }
}
