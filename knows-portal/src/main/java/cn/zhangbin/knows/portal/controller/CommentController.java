package cn.zhangbin.knows.portal.controller;


import cn.zhangbin.knows.portal.exception.ServiceException;
import cn.zhangbin.knows.portal.model.Comment;
import cn.zhangbin.knows.portal.service.ICommentService;
import cn.zhangbin.knows.portal.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.Service;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {

    @Resource
    ICommentService commentService;

    @PostMapping
    public Comment postComment(@Validated CommentVo commentVo, BindingResult result, @AuthenticationPrincipal UserDetails user){
        log.debug("收到的评论信息为:{}",commentVo);
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Comment comment = commentService.saveComment(commentVo,user.getUsername());
        return comment;
    }
}
