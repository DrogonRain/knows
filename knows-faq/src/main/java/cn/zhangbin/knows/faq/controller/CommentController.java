package cn.zhangbin.knows.faq.controller;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.Comment;
import cn.zhangbin.knows.faq.service.ICommentService;
import cn.zhangbin.knows.faq.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/v2/comments")
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

    @GetMapping("/{id}/delete")
    public String remove(@PathVariable Integer id,@AuthenticationPrincipal UserDetails user){
        boolean isDelete = commentService.removeComment(id,user.getUsername());
        if (isDelete){
            return "删除成功!";
        }else {
            return "没有权限删除该评论!";
        }
    }

    @PostMapping("/{id}/update")
    public Comment update(@PathVariable Integer id,@AuthenticationPrincipal UserDetails user,@Validated CommentVo commentVo,BindingResult result){
        log.debug("修改表单信息:{}",commentVo);
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Comment comment = commentService.updateComment(id,commentVo,user.getUsername());
        return comment;
    }
}
