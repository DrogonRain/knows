package cn.zhangbin.knows.sys.service;


import cn.zhangbin.knows.commons.model.Permission;
import cn.zhangbin.knows.commons.model.Role;
import cn.zhangbin.knows.commons.model.User;
import cn.zhangbin.knows.sys.vo.RegisterVo;
import cn.zhangbin.knows.sys.vo.UserVo;
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
public interface IUserService extends IService<User> {

    //业务逻辑层注册学生的方法
    void registerStudent(RegisterVo registerVo);

    // 查询所有讲师的业务逻辑层方法
    List<User> getTeachers();

    //查询包含所有讲师的map对象的方法
    Map<String,User> getTeacherMap();

    User getUserByUserName(String username);

    User getUserById(Integer id);

    //根据用户id查询所有角色
    List<Role> getRolesById(Integer id);

    //根据用户id查询所有权限
    List<Permission> getPermissionsById(Integer id);

    //获取用户信息面板数据的业务逻辑层方法
    UserVo getCurrentUserVo(String username);
}
