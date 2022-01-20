package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.mapper.UserMapper;
import cn.zhangbin.knows.portal.model.Permission;
import cn.zhangbin.knows.portal.model.Role;
import cn.zhangbin.knows.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//导入@Bean组件创建,并实现UserDetailsService接口
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    //导入UserMapper
    @Autowired
    UserMapper userMapper;
    //实现loadUserByUsername()方法约定Spring Security框架的登录实现
    //方法的参数是用户在登录表单输入的用户名,返回值UserDetails是Spring Security
    //框架提供的类型,其中包含了必须要的用户详情
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名获取用户信息
        User user = userMapper.findUserByUsername(username);
        //判断是否输入用户名,未输入则返回null
        if (user == null){
            return null;
        }
        //2.根据用户id获得用户所有权限
        List<Permission> ps = userMapper.findUserPermissionById(user.getId());
        //3.将用户中的所有权限保存在一个数组中,因为框架要求保存在字符串数组中
        String[] auth = new String[ps.size()];
        int i = 0;
        //遍历所有权限添加至数组中
        for (Permission p : ps) {
            auth[i++] = p.getName();
        }
        //3.1添加用户角色信息到auth
        List<Role> roles = userMapper.findUserRolesById(user.getId());
        auth = Arrays.copyOf(auth,auth.length+roles.size());
        //将新角色信息添加到auth中
        for (Role role : roles) {
            auth[i++]=role.getName();
        }
        //4.构建UserDetails对象,并对其主要属性赋值
        UserDetails u = org.springframework.security.core
                .userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
        //将全部权限保存到用户详情
                .authorities(auth)
        //验证Locked值是否为1, 若结果为false则不锁定
                .accountLocked(user.getLocked()==1)
        //验证Enabled是否为0 , 若为0则代表不可用
                .disabled(user.getEnabled()==0).build();
        //5. 返回
        return u;
    }

}
