package cn.zhangbin.knows.portal.service;

import cn.zhangbin.knows.portal.model.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
public interface IUserCollectService extends IService<UserCollect> {

    //根据用户id查询该用户收藏数
    Integer countCollectionsByUserId(Integer userId);
}
