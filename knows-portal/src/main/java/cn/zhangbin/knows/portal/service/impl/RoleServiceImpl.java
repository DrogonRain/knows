package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.model.Role;
import cn.zhangbin.knows.portal.mapper.RoleMapper;
import cn.zhangbin.knows.portal.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
