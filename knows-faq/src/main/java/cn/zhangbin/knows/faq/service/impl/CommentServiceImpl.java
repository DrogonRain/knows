package cn.zhangbin.knows.faq.service.impl;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.Comment;
import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.faq.mapper.CommentMapper;
import cn.zhangbin.knows.faq.service.ICommentService;
import cn.zhangbin.knows.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RibbonClient ribbonClient;

    @Override
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = ribbonClient.getUser(username);
        Comment comment = new Comment()
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setCreatetime(LocalDateTime.now());
        int num = commentMapper.insert(comment);
        if (num !=1){
            throw new ServiceException("数据库异常!");
        }
        return comment;
    }

    @Override
    public boolean removeComment(Integer commentId, String username) {
        User user = ribbonClient.getUser(username);
        //判断是否为讲师,如果不为讲师则无条件删除
        if (user.getType()==1){
            //根据id删除评论
            int num = commentMapper.deleteById(commentId);
            return num==1;
        }
        //若不是讲师,则对比评论发布者id和当前登录的用户id是否相同,按评论id查询评论对象
        Comment comment = commentMapper.selectById(commentId);
        if (comment.getUserId()==user.getId()){
            int num = commentMapper.deleteById(commentId);
            return num==1;
        }
        return false;
    }

    @Override
    public Comment updateComment(Integer commentId, CommentVo commentVo,String username) {
        User user = ribbonClient.getUser(username);
        Comment comment = commentMapper.selectById(commentId);
        if (user.getType().equals(1) || comment.getUserId().equals(user.getId())){
            comment.setContent(commentVo.getContent());
            int num = commentMapper.updateById(comment);
            if (num!=1){
                throw new ServiceException("数据库异常");
            }
            return comment;
        }
        return null;
    }
}
