package cn.zhangbin.knows.portal.service;

import cn.zhangbin.knows.portal.model.Question;
import cn.zhangbin.knows.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
public interface IQuestionService extends IService<Question> {

    //查询当前登录学生问题列表的方法
    PageInfo<Question> getMyQuestion(String username,Integer pageNum,Integer pageSize);

    //学生发布问题的方法
    void saveQuestion(QuestionVo questionVo,String username);

    //查询热门问题
    PageInfo<Question> getHotQuestion(Integer pageNum,Integer pageSize);

    // 根据用户id查询该用户问题数
    Integer countQuestionsByUserId(Integer userId);

    //根据讲师用户名分页查询讲师任务列表
    PageInfo<Question> getTeacherQuestions(String username,Integer pageNum,Integer pageSize);

    //编写根据id获取问题方法
    Question getQuestionById(Integer id);

    //根据id获取问题也详情
    QuestionVo getQuestionVoById(Integer id);

}
