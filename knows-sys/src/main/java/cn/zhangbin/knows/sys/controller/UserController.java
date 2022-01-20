package cn.zhangbin.knows.sys.controller;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.User;

import cn.zhangbin.knows.sys.service.IUserService;
import cn.zhangbin.knows.sys.vo.RegisterVo;
import cn.zhangbin.knows.sys.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
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

    @GetMapping("/getUser/{id}")
    public User getUser(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo, BindingResult result){
        //日志输入接收到表单信息并将表单信息输出到日志中
        log.debug("接收到表单信息为:{}",registerVo);
        if (result.hasErrors()){
            //从错误信息中获得详情
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        try{
            userService.registerStudent(registerVo);
            return "注册完成!";
        }catch (ServiceException e){
            return e.getMessage();
        }
    }

}
