package cn.zhangbin.knows.search.service.impl;

import cn.zhangbin.knows.commons.model.Tag;
import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.search.repository.QuestionRepository;
import cn.zhangbin.knows.search.service.IQuestionService;
import cn.zhangbin.knows.search.utils.Pages;
import cn.zhangbin.knows.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class QuestionServiceImpl implements IQuestionService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private QuestionRepository questionRepository;
    @Override
    public void sync() {
        //查询分页总页数
        String url = "http://faq-service/v2/questions/page/count?pageSize={1}";
        Integer pageSize=8;
        Integer totalPage = restTemplate.getForObject(url,Integer.class,pageSize);
        //开始循环,没循环一次当前页的数据保存到ES中
        for (int i = 0; i < totalPage; i++) {
            url="http://faq-service/v2/questions/page?pageNum={1}&pageSize={2}";
            QuestionVo[] questions = restTemplate.getForObject(url,QuestionVo[].class,i,pageSize);
            //将数组转换为list,否则无法作为方法的参数
            questionRepository.saveAll(Arrays.asList(questions));
            log.debug("已保存第{}页",i);
        }
    }

    public User getUser(String username){
        String url = "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,username);
        return user;
    }

    @Override
    public PageInfo<QuestionVo> search(String username, String key, Integer pageNum, Integer pageSize) {
        //获取用户对象
        User user = getUser(username);
        //设置分页和排序条件
        Pageable pageable = PageRequest.of(pageNum-1,pageSize, Sort.Direction.DESC,"createtime");
        //执行编写好的查询返回分页的结果
        Page<QuestionVo> page = questionRepository.queryAllByParams(key,key,user.getId(),pageable);
        //遍历所有查询出的问题对象添加tags集合
        for (QuestionVo q : page) {
            List<Tag> tags = tagNames2Tags(q.getTagNames());
            q.setTags(tags);
        }
        //将Page类型转为PageInfo返回
        return Pages.pageInfo(page);
    }

    //新加的方法↓↓↓↓↓↓↓↓↓↓↓
//用于获得所有标签
    public Tag[] getTags(){
        String url="http://faq-service/v2/tags";
        Tag[] tags=restTemplate.getForObject(
                url,Tag[].class);
        return tags;
    }
    //新加的方法↓↓↓↓↓↓↓↓↓↓↓
//根据tagNames属性获得List<Tag>对象的方法
    public List<Tag> tagNames2Tags(String tagNames){
        String[] names=tagNames.split(",");
        //获得全部标签数组
        Tag[] tags=getTags();
        //将数组转换为Map类型
        Map<String,Tag> tagMap=new HashMap<>();
        for(Tag t: tags) {
            tagMap.put(t.getName(),t);
        }
        //声明一个List用于接收结果
        List<Tag> questionTags=new ArrayList<>();
        for(String name:names){
            Tag t=tagMap.get(name);
            questionTags.add(t);
        }
        return questionTags;
    }
}
