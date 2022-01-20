package cn.zhangbin.knows.portal.controller;

import cn.zhangbin.knows.portal.exception.ServiceException;
import cn.zhangbin.knows.portal.service.IUserService;
import cn.zhangbin.knows.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

//导入控制层注释及日志注释
@RestController
@Slf4j
public class SystemController {
    /**
     * @Validated表示启动SpringValidation框架对,RegisterVo参数进行验证,验证规则写在了RegisterVo类
     * 中,在RegisterVo参数后紧跟一个BindingResult类型的参数,Spring Validation框架会把验证结果
     * 保存到BindingResult对象中
     */
    @Autowired
    IUserService userService;
    //学生层的控制器方法,POST请求 ,请求路径:/register
    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo, BindingResult result){
        //日志输入接收到表单信息并将表单信息输出到日志中
        log.debug("接收到表单信息为:{}",registerVo);
        if (result.hasErrors()){
            //从错误信息中获得详情
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        try{
            userService.registerStudent(registerVo);
            return "注册完成!";
        }catch (ServiceException e){
            return e.getMessage();
        }
    }

    //获得application.properties文件中声明的数据
    @Value("${knows.resource.path}")
    private File resourceFile;

    @Value("${knows.resource.host}")
    private String resourceHost;

    @PostMapping("/upload/file")
    public String uploadFile(MultipartFile imageFile) throws IOException{
        //按日期创建文件夹
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        //创建文件夹
        File folder = new File(resourceFile,path);
        //创建这个路径
        folder.mkdirs();
        //处理文件名 UUID生成文件名+源文件名后缀
        String fileName = imageFile.getOriginalFilename();
        //截取最后一个 "."及之后的文件扩展名
        String ext = fileName.substring(fileName.lastIndexOf("."));
        //创建UUID字符串并确定文件名
        String name = UUID.randomUUID().toString()+ext;
        //创建上传文件的最终位置
        File file = new File(folder,name);
        //执行上传
        imageFile.transferTo(file);
        //拼接上传成功文件的访问路径并返回
        //http://localhost:8899/2021/11/28/xxx.jpg
        String url = resourceHost+"/"+path+"/"+name;
        log.debug("访问的路径:{}",url);
        return url;
    }
}
