package cn.zhangbin.knows.portal.service;

import cn.zhangbin.knows.portal.model.User;
import cn.zhangbin.knows.portal.vo.RegisterVo;
import cn.zhangbin.knows.portal.vo.UserVo;
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

    //获取用户信息面板数据的业务逻辑层方法
    UserVo getCurrentUserVo(String username);
}
