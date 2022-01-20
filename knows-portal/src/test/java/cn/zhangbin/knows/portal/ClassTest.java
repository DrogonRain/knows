package cn.zhangbin.knows.portal;

import cn.zhangbin.knows.portal.mapper.AnswerMapper;
import cn.zhangbin.knows.portal.mapper.ClassroomMapper;
import cn.zhangbin.knows.portal.model.Answer;
import cn.zhangbin.knows.portal.model.Classroom;
import cn.zhangbin.knows.portal.model.Question;
import cn.zhangbin.knows.portal.model.Tag;
import cn.zhangbin.knows.portal.service.IAnswerService;
import cn.zhangbin.knows.portal.service.IQuestionService;
import cn.zhangbin.knows.portal.service.ITagService;
import cn.zhangbin.knows.portal.service.IUserService;
import cn.zhangbin.knows.portal.vo.QuestionVo;
import cn.zhangbin.knows.portal.vo.RegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ClassTest {

    //导入classroomMapper
    @Autowired
    ClassroomMapper classroomMapper;

    @Autowired
    IUserService userService;

    @Autowired
    IQuestionService questionService;

    @Autowired
    ITagService tagService;

    /**
     * eq():equals 等于
     *
     * gt():great than大于
     *
     * lt():less than 小于
     *
     * ge():great equals大于等于
     *
     * le():less equals小于等于
     *
     * ne():not equals不等于
     */
    @Resource
    AnswerMapper answerMapper;

    @Resource
    IAnswerService answerService;
    @Test
    public void test1(){
        boolean a = answerService.accept(82,"tc2");
        System.out.println(a);
    }
    @Test
    public void testMapper(){
        List<Answer> answers = answerMapper.findAnswersWithCommentByQuestionId(149);
        for (Answer a:answers){
            System.out.println(a);
        }
    }
    @Test
    public void testQuery(){
        //QueryWrapper实际上是一个可以包含查询条件的对象
        //1. 实例化QueryWrapper
        QueryWrapper<Classroom> query = new QueryWrapper<>();
        //2. 设置条件为: 邀请码--JSD2002-525416
        query.eq("invite_code","JSD2002-525416");
        //3. 使用Mybatis Plus框架规定的方法,QueryWrapper实例对象作为参数传入
        Classroom classroom = classroomMapper.selectOne(query);
        //4. 输出查询结果
        System.out.println(classroom);
    }

    @Test
    public void getQuestionVo(){
        Integer id = 152;
        QuestionVo questionVo = questionService.getQuestionVoById(id);
        System.out.println(questionVo);
    }

    @Test
    public void add(){
        RegisterVo registerVo = new RegisterVo();
        registerVo.setPhone("13311313669");
        registerVo.setNickname("大苍");
        registerVo.setInviteCode("JSD2002-525416");
        registerVo.setPassword("888888");
        userService.registerStudent(registerVo);
        System.out.println("ok!");
    }

    @Test
    public void myQuestion(){
//        List<Question> list = questionService.getMyQuestion("st2");
//        list.forEach(question -> System.out.println(question));
        PageInfo<Question> list = questionService.getHotQuestion(1,6);
        list.getList().forEach(question -> System.out.println(question));
    }

    @Test
    public void getTags(){
//        List<Tag> tags = tagService.getTags();
//        tags.forEach(tag -> System.out.println(tag));
//        Map<String,Tag> map = tagService.getTagMap();
//        map.forEach((k,v) -> System.out.println(k+"的值为:  "+v));
//        List<Question> list = questionService.getMyQuestion("st2");
//        list.forEach(question -> System.out.println(question.getTags()));
    }
}
