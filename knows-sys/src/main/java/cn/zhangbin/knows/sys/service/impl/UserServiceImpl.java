package cn.zhangbin.knows.sys.service.impl;


import cn.zhangbin.knows.commons.exception.ServiceException;
import cn.zhangbin.knows.commons.model.*;
import cn.zhangbin.knows.sys.mapper.ClassroomMapper;
import cn.zhangbin.knows.sys.mapper.UserMapper;
import cn.zhangbin.knows.sys.mapper.UserRoleMapper;
import cn.zhangbin.knows.sys.service.IUserService;
import cn.zhangbin.knows.sys.vo.RegisterVo;
import cn.zhangbin.knows.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //导入classroomMapper,userMapper,userRoleMapper
    @Autowired
    ClassroomMapper classroomMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    private RestTemplate restTemplate;

    //定义密码加密方法PasswordEncoder
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    //注册学生的实现方法
    @Override
    public void registerStudent(RegisterVo registerVo) {
        //1. 验证邀请码正确与否,实例化QueryWrapper,验证invite_code是否正确
        QueryWrapper<Classroom> query = new QueryWrapper<>();
        // 1.1 查询班级
        query.eq("invite_code",registerVo.getInviteCode());
        // 1.2 如果查询出的班级为空,输出"邀请码错误!"
        Classroom classroom = classroomMapper.selectOne(query);
        if (classroom == null){
            throw new ServiceException("邀请码错误!");
        }
        //2. 验证手机号是否注册过
        // 2.1通过手机号查询用户
        User user = userMapper.findUserByUsername(registerVo.getPhone());
        // 2.2 如果角色信息不为空,输出"手机号已经注册过!"
        if (user != null){
            throw new ServiceException("手机号已经注册过!");
        }
        //3. 用户输入的密码进行加密,使用encode方法
        String bcrypt = "{bcrypt}"+encoder.encode(registerVo.getPassword());
        //4. 构建User对象并赋值 电话,昵称,密码,classroomid,创建时间.enabled为1,locked为0,type为0
        User u = new User();
        u.setUsername(registerVo.getPhone());
        u.setNickname(registerVo.getNickname());
        u.setPassword(bcrypt);
        u.setClassroomId(classroom.getId());
        u.setCreatetime(LocalDateTime.now());
        u.setEnabled(1);
        u.setLocked(0);
        u.setType(0);
        //5. user新增到数据库,并检查新增的自增列的值,如果不等于1则,输出"数据库异常,注册失败!";
        int num = userMapper.insert(u);
        if (num!=1){
            throw new ServiceException("数据库异常,注册失败!");
        }
        //6. 构建用户和觉得关系对象并新增到关系表,设置Userid,roleid为2
        UserRole userRole = new UserRole();
        userRole.setUserId(u.getId());
        userRole.setRoleId(2);
        // 6.1 插入对象,如果插入结果自增列的值不为1则输出"数据库异常,注册失败!";
        num = userRoleMapper.insert(userRole);
        if (num!=1){
            throw new ServiceException("数据库异常,注册失败!");
        }
    }

    @Override
    public List<User> getTeachers() {
        if (teachers.isEmpty()){
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("type",1);
            List<User> list = userMapper.selectList(query);
            teachers.addAll(list);
            for (User u : list){
                teacherMap.put(u.getNickname(),u);
            }
        }
        return teachers;
    }

    //利用Timer周期调用方法,每隔三十分钟清空一次缓存,以保证30分钟和数据库同步一次
    Timer timer = new Timer();

    //静态块是当前类加载到java虚拟机时运行一次,一般情况下,一个类只会加载到JVM一次,所以静态块中的代码也只运行一次
    {
        //这里是普通初始化块,和java虚拟机加载无关
        //只要实例化当前类对象,在运行构造方法之前会先运行这里的代码
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (teachers) {
                    synchronized (teacherMap){
                        teachers.clear();
                        teacherMap.clear();
                    }
                }
                System.out.println("缓存已清空!");
            }
        },1000*60*30,1000*60*30);
    }

    private List<User> teachers = new CopyOnWriteArrayList<>();
    private Map<String,User> teacherMap = new ConcurrentHashMap<>();
    @Override
    public Map<String, User> getTeacherMap() {
        if (teacherMap.isEmpty()){
            getTeachers();
        }
        return teacherMap;
    }

    @Override
    public User getUserByUserName(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public List<Role> getRolesById(Integer id) {
        return userMapper.findUserRolesById(id);
    }

    @Override
    public List<Permission> getPermissionsById(Integer id) {
        return userMapper.findUserPermissionById(id);
    }

    @Override
    public UserVo getCurrentUserVo(String username) {
        //获取userVo对象
        UserVo userVo = userMapper.findUserVoByUsername(username);
        //按查询出的userVo中的id查询这个用户的问题数,收藏数及回答数
        String url = "http://faq-service/v2/questions/count?userId={1}";
        Integer num = restTemplate.getForObject(url,Integer.class,userVo.getId());
        url = "http://faq-service/v2/questions/countCollections?userId={1}";
        Integer collections = restTemplate.getForObject(url,Integer.class,userVo.getId());
        url = "http://faq-service/v2/questions/countAnswers?userId={1}";
        Integer answers = restTemplate.getForObject(url,Integer.class,userVo.getId());
        //将查询出的问题数,收藏数及回答数赋值userVo
        userVo.setQuestions(num);
        userVo.setCollections(collections);
        userVo.setAnswers(answers);
        //返回
        return userVo;
    }
}
