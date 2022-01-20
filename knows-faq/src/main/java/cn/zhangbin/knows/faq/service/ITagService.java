package cn.zhangbin.knows.faq.service;


import cn.zhangbin.knows.commons.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
public interface ITagService extends IService<Tag> {

    //全查所有标签的业务逻辑方法
    List<Tag> getTags();

    //全查所有标签返回Map的业务逻辑层方法
    Map<String,Tag> getTagMap();
}
