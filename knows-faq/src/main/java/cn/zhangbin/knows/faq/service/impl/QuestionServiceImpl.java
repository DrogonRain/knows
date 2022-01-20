package cn.zhangbin.knows.faq.service.impl;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.*;
import cn.zhangbin.knows.faq.mapper.QuestionMapper;
import cn.zhangbin.knows.faq.mapper.QuestionTagMapper;
import cn.zhangbin.knows.faq.mapper.UserQuestionMapper;
import cn.zhangbin.knows.faq.service.IQuestionService;
import cn.zhangbin.knows.faq.service.ITagService;
import cn.zhangbin.knows.faq.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    ITagService tagService;

    @Autowired
    private QuestionTagMapper questionTagMapper;

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Override
    public PageInfo<Question> getHotQuestion(Integer pageNum, Integer pageSize) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("delete_status",0);
        query.orderByDesc("page_views");
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list = questionMapper.selectList(query);
        return new PageInfo<>(list);
    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        Integer count = questionMapper.selectCount(query);
        return count;
    }

    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
        User user = getUser(username);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.findTeacherQuestions(user.getId());
        //根据Question对象的tagNames属性获得问题列表中的所有标签集合
        for (Question question : questions) {
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);
        }
        return new PageInfo<>(questions);
    }

    @Override
    public Question getQuestionById(Integer id) {
        Question question = questionMapper.selectById(id);
        List<Tag> tags = tagName2Tags(question.getTagNames());
        question.setTags(tags);
        return question;
    }

    @Override
    @Transactional
    public QuestionVo getQuestionVoById(Integer id) {
        Question question = questionMapper.selectById(id);
        String title = question.getTitle();
        String[] tags = question.getTagNames().split(",");
        QueryWrapper<UserQuestion> query = new QueryWrapper<>();
        query.eq("question_id",question.getId());
        List<UserQuestion> userQuestions = userQuestionMapper.selectList(query);
        String[] teachers = new String[userQuestions.size()];
        int j = 0;
        for (int i = 0; i < userQuestions.size(); i++) {
            String url = "http://sys-service/v1/users/getUser/{1}";
            Integer userId = userQuestions.get(i).getUserId();
            User user = restTemplate.getForObject(url,User.class,userId);
            if (user.getType()==1){
                teachers[j] = user.getNickname();
                j++;
            }
        }
        String content = question.getContent();
        QuestionVo questionVo = new QuestionVo()
                .setTitle(title)
                .setTagNames(tags)
                .setTeacherNames(teachers)
                .setContent(content);
        return questionVo;
    }

    @Override
    public PageInfo<Question> getQuestions(Integer pageNum, Integer pageSize) {
        //设置查询的页码
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list = questionMapper.selectList(null);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Question> getMyQuestion(String username,Integer pageNum,Integer pageSize) {
        //1.根据用户名查询用户信息
        User user = getUser(username);
        //1.1 判断如果用户为空则抛出异常用户不存在!
        if (user == null){
            throw new ServiceException("用户不存在!");
        }
        //2.根据用户id查询用户的所有问题
        QueryWrapper<Question> query = new QueryWrapper<>();
        //2.2设置条件,user_id为当前登录用户的id,查询出的问题不能是是被删除的即delete_status为0并按创建时间降序排列
        query.eq("user_id",user.getId());
        query.eq("delete_status",0);
        query.orderByDesc("createtime");
        PageHelper.startPage(pageNum,pageSize);
        //2.3按照条件进行问题查询
        List<Question> list = questionMapper.selectList(query);
        //遍历list中所有问题,为每个问题的标签集合赋值
        for (Question question : list) {
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);
        }
        //返回查询结果
        return new PageInfo<>(list);
    }

    @Override
    /**
     * @Transactional功能是保证当前方法中所有数据库操作要么都执行,要么都不执行.
     * 如果没有发生异常就都执行,如果任何位置发生了任何异常,就都不执行
     * 如果发生异常时已经有数据库操作完成了,这个操作会自动取消(回滚)
     * 使用时机:业务逻辑层方法
     * 如果方法中有增删改方法,尤其是两次或以上的增删改操作时
     */
    @Transactional
    public void saveQuestion(QuestionVo questionVo, String username) {
        //1.根据用户名获取用户信息
        User user = getUser(username);
        //2.将用户选中的标签拼接位字符串,中间用逗号分割
        StringBuilder builder = new StringBuilder();
        for (String tagName : questionVo.getTagNames()) {
            builder.append(tagName).append(",");
        }
        //2.1删除最后一位的逗号后将其转为字符串
        String tagNames = builder.deleteCharAt(builder.length()-1).toString();
        //3.构造Question对象
        Question question = new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setTagNames(tagNames)
                .setStatus(0)
                .setPageViews(0)
                .setDeleteStatus(0)
                .setPublicStatus(0);
        //4.执行新增
        int num = questionMapper.insert(question);
        //4.1如果新增的条数不等于1则抛出"数据库异常!"
        if (num!=1){
            throw new ServiceException("数据库异常!");
        }
        //5.执行和标签关系表的新增,先获取包含所有标签对象的map
        Map<String,Tag> tagMap = tagService.getTagMap();
        //5.1遍历根据标签名获取所有的标签对象
        for (String tagName : questionVo.getTagNames()){
            Tag t = tagMap.get(tagName);
            QuestionTag questionTag = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(t.getId());
            num = questionTagMapper.insert(questionTag);
            //5.2为中间关系表添加内容并检查结果新增条数是否为1,若不为1则抛出"数据库异常!"
            if (num!=1){
                throw new ServiceException("数据库异常!");
            }
        }
        //6.执行和用户表关系的新增,先获取讲师的map
        String url = "http://sys-service/v1/users/master";
        User[] users = restTemplate.getForObject(url,User[].class);
        //实例化讲师集合
        Map<String,User> teacherMap = new HashMap<>();
        //遍历讲师数组为map赋值
        for (User u : users){
            teacherMap.put(u.getNickname(),u);
        }
        //6.1遍历所有昵称获取讲师对象,并设置用户问题表的问题id和用户id以及创建时间为当前时间
        //添加至数据库如果添加结果不为1则抛出"数据库异常!"
        for (String nickName : questionVo.getTeacherNames()){
            User teacher = teacherMap.get(nickName);
            UserQuestion userQuestion = new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(teacher.getId())
                    .setCreatetime(LocalDateTime.now());
            num = userQuestionMapper.insert(userQuestion);
            if (num!=1){
                throw new ServiceException("数据库异常!");
            }
        }
    }



    //编写专门处理标签名称字符串转为List<Tag>的方法
    private List<Tag> tagName2Tags(String tagNames){
        String[] names = tagNames.split(",");
        //声明接受对应标签的list
        List<Tag> tags = new ArrayList<>();
        //包含所有标签的map
        Map<String,Tag> tagMap = tagService.getTagMap();
        //遍历names数组将数组元素对应的tag对象保存到tags集合中
        for (String key : names) {
            tags.add(tagMap.get(key));
        }
        return tags;
    }

    @Autowired
    private RestTemplate restTemplate;
    public User getUser(String username){
        //设置请求地址
        String url = "http://sys-service/v1/auth/user?username={1}";
        //根据用户名查询用户
        User user = restTemplate.getForObject(url,User.class,username);
        return user;//返回查询到的用户信息
    }
}
