package cn.zhangbin.knows.portal.controller;


import cn.zhangbin.knows.portal.exception.ServiceException;
import cn.zhangbin.knows.portal.model.Question;
import cn.zhangbin.knows.portal.service.IQuestionService;
import cn.zhangbin.knows.portal.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @GetMapping("/questionVo/{id}")
    public QuestionVo getQuestionVo(@PathVariable Integer id){
        if (id == null){
            throw new ServiceException("id无效!");
        }
        QuestionVo questionVo = questionService.getQuestionVoById(id);
        return questionVo;
    }

    @GetMapping("/my")
    // @AuthenticationPrincipal注解的含义是获得当前登录用户的详情,注解后跟UserDetails即可
    public PageInfo<Question> getMyQuestions(@AuthenticationPrincipal UserDetails user,Integer pageNum){
        if (pageNum==null)
            pageNum =1;
        Integer pageSize = 8;
        PageInfo<Question> pageInfo = questionService.getMyQuestion(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    @GetMapping("/hot")
    public PageInfo<Question> getHostQuestions(){
        Integer pageSize = 10;
        PageInfo<Question> pageInfo = questionService.getHotQuestion(1,pageSize);
        return pageInfo;
    }

    @GetMapping("/teacher")
    //@PreAuthorize设置讲师的任务列表学生不能查询,必须拥有讲师身份才能运行这个方法
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public PageInfo<Question> teachers(@AuthenticationPrincipal UserDetails user,Integer pageNum){
        if (pageNum==null){
            pageNum = 1;
        }
        Integer pageSize = 8;
        PageInfo<Question> pageInfo = questionService.getTeacherQuestions(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    @PostMapping("")
    public String createQuestion(@Validated QuestionVo questionVo, BindingResult result,@AuthenticationPrincipal UserDetails user){
        log.debug("接收到表单信息:{}",questionVo);
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        try {
            //调用业务逻辑层
            questionService.saveQuestion(questionVo,user.getUsername());
            return "ok";
        }catch (ServiceException e){
            return e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable Integer id){
        if (id==null){
            throw new ServiceException("id无效!");
        }
        Question question = questionService.getQuestionById(id);
        return question;
    }
}
