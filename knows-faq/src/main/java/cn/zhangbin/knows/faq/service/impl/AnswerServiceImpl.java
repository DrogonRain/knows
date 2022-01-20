package cn.zhangbin.knows.faq.service.impl;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.Answer;
import cn.zhangbin.knows.commons.model.Question;
import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.faq.mapper.AnswerMapper;
import cn.zhangbin.knows.faq.mapper.QuestionMapper;
import cn.zhangbin.knows.faq.service.IAnswerService;
import cn.zhangbin.knows.faq.vo.AnswerVo;
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
    private RibbonClient ribbonClient;

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
        User user = ribbonClient.getUser(username);
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
        User user = ribbonClient.getUser(username);
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
