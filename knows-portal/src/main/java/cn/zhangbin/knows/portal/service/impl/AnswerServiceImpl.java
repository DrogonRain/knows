package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.exception.ServiceException;
import cn.zhangbin.knows.portal.mapper.QuestionMapper;
import cn.zhangbin.knows.portal.mapper.UserMapper;
import cn.zhangbin.knows.portal.model.Answer;
import cn.zhangbin.knows.portal.mapper.AnswerMapper;
import cn.zhangbin.knows.portal.model.Question;
import cn.zhangbin.knows.portal.model.User;
import cn.zhangbin.knows.portal.service.IAnswerService;
import cn.zhangbin.knows.portal.vo.AnswerVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public Integer countAnswersByUserId(Integer userId) {
        QueryWrapper<Answer> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        Integer count = answerMapper.selectCount(query);
        return count;
    }

    @Override
    @Transactional
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        //根据用户名查询用户
        User user = userMapper.findUserByUsername(username);
        //设置答案的属性
        Answer answer = new Answer()
                .setQuestId(answerVo.getQuestionId())
                .setContent(answerVo.getContent())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now())
                .setLikeCount(0)
                .setAcceptStatus(0);
        //插入回答并检查插入结果
        int num = answerMapper.insert(answer);
        if (num!=1){
            throw new ServiceException("数据库异常!");
        }
        //返回回答
        return answer;
    }

    @Override
    public List<Answer> getQuestionAnswers(Integer questionId) {
        List<Answer> answers = answerMapper.findAnswersWithCommentByQuestionId(questionId);
        return answers;
    }

    @Override
    @Transactional
    public boolean accept(Integer answerId, String username) {
        Answer answer = answerMapper.selectById(answerId);
        User user = userMapper.findUserByUsername(username);
        Question question = questionMapper.selectById(answer.getQuestId());
        if (question.getUserId()!=user.getId()){
            throw new ServiceException("您不是问题发起人,无法进行该操作!");
        }else if (answer == null || answer.getAcceptStatus()==1){
            return false;
        }
        //修改answer表的采纳状态
        int num = answerMapper.updateAcceptStatus(1,answerId);
        if (num!=1){
            throw new ServiceException("服务器繁忙,请稍后重试!");
        }
        //修改question表的状态
        num = questionMapper.updateStatus(Question.SOLVED,answer.getQuestId());
        if (num!=1){
            throw new ServiceException("服务器繁忙,请稍后重试!");
        }
        return true;
    }
}
