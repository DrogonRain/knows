package cn.zhangbin.knows.faq.service;


import cn.zhangbin.knows.commons.model.Answer;
import cn.zhangbin.knows.faq.vo.AnswerVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
public interface IAnswerService extends IService<Answer> {

    //根据用户id查询用户回答数
    Integer countAnswersByUserId(Integer userId);

    //新增回答的业务逻辑层方法,返回值为Answer,用户直接将新增成功的回答显示在页面上
    Answer saveAnswer(AnswerVo answerVo, String username);

    //查看全部答案
    List<Answer> getQuestionAnswers(Integer questionId);

    /**
     * 采纳答案的方法
     * 如果想做的严谨一些,可以传入当前登录用户的username
     * 来判断是不是问题的提问者在采纳答案
     */
    boolean accept(Integer answerId,String username);
}
