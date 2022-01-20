package cn.zhangbin.knows.faq;

import cn.zhangbin.knows.commons.model.Tag;
import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.faq.mapper.TagMapper;
import cn.zhangbin.knows.faq.service.IQuestionService;
import cn.zhangbin.knows.faq.vo.QuestionVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KnowsFaqApplicationTests {

    @Resource
    RedisTemplate<String, List<Tag>> redisTemplate;

    @Resource
    TagMapper tagMapper;

    @Resource
    IQuestionService questionService;

    @Test
    void contextLoads() {
        //全查所有标签
        List<Tag> tags = tagMapper.selectList(null);
        redisTemplate.opsForValue().set("alltag",tags);
        //向redis中保存数据
        //例如: redisTemplate.opsForValue().set("myname","张三丰");
        System.out.println("ok");
    }

    @Test
    void getName(){
        List<Tag> tags = redisTemplate.opsForValue().get("alltag");
        for (Tag tag : tags){
            System.out.println(tag);
        }
    }

    @Resource
    RestTemplate restTemplate;

    @Test
    void getUser(){
        String url = "http://sys-service/v1/auth/user?username={1}";
        //第三个参数开始向{1}中的占位符中赋值
        User user = restTemplate.getForObject(url,User.class,"st2");
        System.out.println(user);
    }

    @Test
    void getQuestionVo(){
        QuestionVo questionVo = questionService.getQuestionVoById(10);
        System.out.println(questionVo);
    }

}
