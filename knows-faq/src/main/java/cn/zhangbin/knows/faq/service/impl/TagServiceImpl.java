package cn.zhangbin.knows.faq.service.impl;


import cn.zhangbin.knows.commons.model.Tag;
import cn.zhangbin.knows.faq.mapper.TagMapper;
import cn.zhangbin.knows.faq.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    //CopyOnWriteArrayList是一个溪县城安全的集合类型jdk1.8加入
    private List<Tag> tags = new CopyOnWriteArrayList<>();
    //ConcurrentHashMap是一个溪县城安全的HashMap类型jdk1.8加入
    private Map<String,Tag> tagMap = new ConcurrentHashMap<>();

    @Autowired
    private TagMapper tagMapper;

    @Resource
    private RedisTemplate<String,List<Tag>> redisTemplate;

    @Override
    public List<Tag> getTags() {
        List<Tag> tags = redisTemplate.opsForValue().get("tags");
        if (tags==null||tags.isEmpty()){
            System.out.println("连接数据库新增Redis中内容");
            tags=list();//list()方法是当前类父类提供的,就是全查所有标签
            //将查询到的心肺保存至redis
            redisTemplate.opsForValue().set("tags",tags);
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
        Map<String,Tag> map = new HashMap<>();
        for (Tag t : getTags()){
            map.put(t.getName(),t);
        }
        return map;
    }
}
