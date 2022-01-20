package cn.zhangbin.knows.sys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller标记的控制器类中可以使用返回字符串的方式跳转页面

@RestController
@Slf4j
@RequestMapping("/v1/home")
public class HomeController {

    //Spring Security框架提供了判断是否拥有权限或角色的类型
    public static final GrantedAuthority STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER = new SimpleGrantedAuthority("ROLE_TEACHER");

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails user){
        //判断如果为老师,返回老师界面
        if (user.getAuthorities().contains(TEACHER)){
            return "/index_teacher.html";
            //学生则返回学生页面
        }else if(user.getAuthorities().contains(STUDENT)){
            return "/index_student.html";
        }else {
            //不是老师也不是学生则返回登录页面
            return "/login.html";
        }
    }
}
