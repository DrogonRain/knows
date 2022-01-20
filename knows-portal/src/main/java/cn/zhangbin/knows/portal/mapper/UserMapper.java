package cn.zhangbin.knows.portal.mapper;

import cn.zhangbin.knows.portal.model.Permission;
import cn.zhangbin.knows.portal.model.Role;
import cn.zhangbin.knows.portal.model.User;
import cn.zhangbin.knows.portal.vo.UserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author zhangbin.cn
* @since 2021-11-23
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {

        //根据用户名称查询用户
        @Select("select * from user where username=#{username}")
        User findUserByUsername(String username);

        //根据用户id查询用户权限
        @Select("SELECT p.id, p.name" +
                " FROM user u" +
                " LEFT JOIN user_role ur ON u.id = ur.user_id" +
                " LEFT JOIN role r ON r.id = ur.role_id" +
                " LEFT JOIN role_permission rp ON r.id = rp.role_id" +
                " LEFT JOIN permission p ON p.id = rp.permission_id" +
                " WHERE u.id=#{id}")
        List<Permission> findUserPermissionById(Integer id);

        //根据用户名查询UserVo类型对象,UserVo用户信息只有id,username,nickname,返回类型为UserVo
        @Select("select id,username,nickname from user where username=#{username}")
        UserVo findUserVoByUsername(String username);

        //根据用户名查询UserVo类型对象,UserVo用户信息只有id,username,nickname,返回类型为UserVo
        @Select("select id,username,nickname,type from user where id=#{id}")
        User findUserById(Integer id);

        @Select("SELECT r.id,r.name" +
                " FROM user u" +
                " LEFT JOIN user_role ur ON ur.user_id=u.id" +
                " LEFT JOIN role r ON r.id=ur.role_id" +
                " WHERE u.id=#{id}")
        List<Role> findUserRolesById(Integer id);

    }
