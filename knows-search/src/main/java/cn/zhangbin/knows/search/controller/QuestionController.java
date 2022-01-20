package cn.zhangbin.knows.search.controller;

import cn.zhangbin.knows.search.service.IQuestionService;
import cn.zhangbin.knows.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/v3/questions")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;
    @PostMapping
    public PageInfo<QuestionVo> search(@AuthenticationPrincipal UserDetails user,String key,Integer pageNum){
        if (pageNum==null){
            pageNum=1;
        }
        Integer pageSize = 8;
        PageInfo<QuestionVo> pageInfo = questionService.search(user.getUsername(),key,pageNum,pageSize);
        return pageInfo;
    }
}
