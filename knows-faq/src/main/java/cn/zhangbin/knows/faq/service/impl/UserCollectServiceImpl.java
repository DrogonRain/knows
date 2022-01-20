package cn.zhangbin.knows.faq.service.impl;


import cn.zhangbin.knows.commons.model.UserCollect;
import cn.zhangbin.knows.faq.mapper.UserCollectMapper;
import cn.zhangbin.knows.faq.service.IUserCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

    @Autowired
    UserCollectMapper collectMapper;

    @Override
    public Integer countCollectionsByUserId(Integer userId) {
        QueryWrapper<UserCollect> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        Integer count = collectMapper.selectCount(query);
        return count;
    }
}
