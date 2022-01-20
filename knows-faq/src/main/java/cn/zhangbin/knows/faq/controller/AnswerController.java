package cn.zhangbin.knows.faq.controller;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.Answer;
import cn.zhangbin.knows.faq.service.IAnswerService;
import cn.zhangbin.knows.faq.vo.AnswerVo;
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
@RequestMapping("/v2/answers")
@Slf4j
public class AnswerController {

    @Autowired
    IAnswerService answerService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public Answer postAnswer(@Validated AnswerVo answerVo, BindingResult result, @AuthenticationPrincipal UserDetails user){
        log.debug("接受表单信息:{}",answerVo);
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Answer answer = answerService.saveAnswer(answerVo,user.getUsername());
        return answer;
    }

    @GetMapping("/question/{id}")
    public List<Answer> questionAnswers(@PathVariable Integer id){
        List<Answer> answers = answerService.getQuestionAnswers(id);
        return answers;
    }

    //采纳答案案的控制层方法
    @GetMapping("/{answerId}/solved")
    public String solved(@AuthenticationPrincipal UserDetails user, @PathVariable Integer answerId){
        boolean isAccept = answerService.accept(answerId,user.getUsername());
        if (isAccept){
            return "采纳完成";
        }else{
            return "采纳失败,检查回答存在并且没有被采纳过";
        }
    }
}
