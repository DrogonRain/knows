package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.model.UserCollect;
import cn.zhangbin.knows.portal.mapper.UserCollectMapper;
import cn.zhangbin.knows.portal.service.IUserCollectService;
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
