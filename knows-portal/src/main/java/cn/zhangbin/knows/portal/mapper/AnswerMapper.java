package cn.zhangbin.knows.portal.mapper;

import cn.zhangbin.knows.portal.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
    public interface AnswerMapper extends BaseMapper<Answer> {

        //根据问题id查询这个问题对应的所有回答和回答对应的所有评论的方法
        List<Answer> findAnswersWithCommentByQuestionId(Integer questionId);
        //因为方法并没有写注解的SQL语句,所以MyBatis框架会到对应的AnswerMapper.xml文件中去进行查找

        //修改指定id的回答的采纳状态
        @Update("update answer set accept_status=#{acceptStatus} where id=#{answerId}")
        Integer updateAcceptStatus(@Param("acceptStatus") Integer acceptStatus,@Param("answerId") Integer answerId);
    }
