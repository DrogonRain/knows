package cn.zhangbin.knows.portal.controller;


import cn.zhangbin.knows.portal.model.User;
import cn.zhangbin.knows.portal.service.IUserService;
import cn.zhangbin.knows.portal.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/v1/users")//设置路径请求前缀
public class UserController {

    @GetMapping("/get")
    public String get(){
        return "hello";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('/abc')")
    public String list(){
        return "所有用户信息";
    }

    @Autowired
    private IUserService userService;

    @GetMapping("/master")
    public List<User> getTeachers(){
        List<User> list = userService.getTeachers();
        return list;
    }

    @GetMapping("/me")
    public UserVo me(@AuthenticationPrincipal UserDetails user){
        UserVo userVo = userService.getCurrentUserVo(user.getUsername());
        return userVo;
    }

}
