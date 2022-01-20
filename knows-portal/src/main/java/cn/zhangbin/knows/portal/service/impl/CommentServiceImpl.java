package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.exception.ServiceException;
import cn.zhangbin.knows.portal.mapper.UserMapper;
import cn.zhangbin.knows.portal.model.Comment;
import cn.zhangbin.knows.portal.mapper.CommentMapper;
import cn.zhangbin.knows.portal.model.User;
import cn.zhangbin.knows.portal.service.ICommentService;
import cn.zhangbin.knows.portal.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

    @Override
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = userMapper.findUserByUsername(username);
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
}
