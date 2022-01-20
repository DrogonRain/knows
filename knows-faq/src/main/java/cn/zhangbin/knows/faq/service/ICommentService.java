package cn.zhangbin.knows.faq.service;


import cn.zhangbin.knows.commons.model.Comment;
import cn.zhangbin.knows.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
public interface ICommentService extends IService<Comment> {

    //新增评论的业务逻辑层方法
    Comment saveComment(CommentVo commentVo, String username);

    //删除评论
    boolean removeComment(Integer commentId,String username);

    //修改评论
    Comment updateComment(Integer commentId,CommentVo commentVo,String username);
}
