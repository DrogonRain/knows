package cn.zhangbin.knows.faq.mapper;


import cn.zhangbin.knows.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author zhangbin.cn
* @since 2021-11-23
*/
    @Repository
    public interface QuestionMapper extends BaseMapper<Question> {

        //根据讲师id查询讲师任务列表的mapper方法
        @Select("SELECT q.* " +
                " FROM question q" +
                " LEFT JOIN user_question uq" +
                " ON q.id=uq.question_id" +
                " WHERE q.user_id=#{id} OR uq.user_id=#{id}" +
                " ORDER BY q.createtime DESC")
        List<Question> findTeacherQuestions(Integer id);

        //修改状态的方法
        @Update("update question set status=#{status} where id=#{questionId}")
        Integer updateStatus(@Param("status") Integer status, @Param("questionId") Integer questionId);
    }
