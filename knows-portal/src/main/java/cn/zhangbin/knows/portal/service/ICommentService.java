package cn.zhangbin.knows.portal.service;

import cn.zhangbin.knows.portal.model.Comment;
import cn.zhangbin.knows.portal.vo.CommentVo;
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
    Comment saveComment(CommentVo commentVo,String username);
}
