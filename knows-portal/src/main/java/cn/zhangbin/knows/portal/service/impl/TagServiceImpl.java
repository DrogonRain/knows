package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.model.Tag;
import cn.zhangbin.knows.portal.mapper.TagMapper;
import cn.zhangbin.knows.portal.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Tag> getTags() {
        //判断tags是否为空
        if (tags.isEmpty()){
            //从数据库当中查询出所有标签
            synchronized (tags){
                if (tags.isEmpty()){
                    List<Tag> tags = tagMapper.selectList(null);
                    //将查询出的所有标签保存在线程安全的集合中
                    this.tags.addAll(tags);
                    for (Tag t : tags) {
                        tagMap.put(t.getName(),t);
                    }
                }
            }
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
        if (tagMap.isEmpty()){
            getTags();
        }
        return tagMap;
    }
}
