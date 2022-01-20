package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.model.Permission;
import cn.zhangbin.knows.portal.mapper.PermissionMapper;
import cn.zhangbin.knows.portal.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
