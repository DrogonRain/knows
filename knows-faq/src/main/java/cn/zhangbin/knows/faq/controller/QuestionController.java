package cn.zhangbin.knows.faq.controller;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.Question;
import cn.zhangbin.knows.faq.service.IAnswerService;
import cn.zhangbin.knows.faq.service.IQuestionService;
import cn.zhangbin.knows.faq.service.IUserCollectService;
import cn.zhangbin.knows.faq.vo.QuestionVo;
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
@RequestMapping("/v2/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IUserCollectService userCollectService;

    @Autowired
    private IAnswerService answerService;

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
    public PageInfo<Question> getMyQuestions(@AuthenticationPrincipal UserDetails user, Integer pageNum){
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

    //根据用户id查询用户的问题数的控制层方法
    @GetMapping("/count")
    public Integer countQuestions(Integer userId){
        Integer num=questionService.countQuestionsByUserId(userId);
        return num;
    }

    //根据用户id查询用户的收藏数
    @GetMapping("/countCollections")
    public Integer countCollections(Integer userId){
        Integer collections= userCollectService.countCollectionsByUserId(userId);
        return collections;
    }

    //根据用户id查询用户的回答数
    @GetMapping("/countAnswers")
    public Integer countAnswers(Integer userId){
        Integer answers = answerService.countAnswersByUserId(userId);
        return answers;
    }

    @PostMapping("")
    public String createQuestion(@Validated QuestionVo questionVo, BindingResult result, @AuthenticationPrincipal UserDetails user){
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

    //根据页码分页显示查询全部问题
    @GetMapping("/page")
    public List<Question> questions(Integer pageNum,Integer pageSize){
        PageInfo<Question> pageInfo = questionService.getQuestions(pageNum,pageSize);
        return pageInfo.getList();
    }

    //根据每页条数查询总页数
    @GetMapping("/page/count")
    public Integer pageCount(Integer pageSize){
        //questionService.count()是MyBaitsPlus提供的方法,能够返回当前表的总条数
        int count = questionService.count();
        return count%pageSize==0?count/pageSize:count/pageSize+1;
    }
}
