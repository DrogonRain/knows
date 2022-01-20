package cn.zhangbin.knows.resource.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/upload")
@CrossOrigin
public class ImageController {
    /**
     * 之前为resource项目访问任何资源添加了一个前缀
     * 例如: localhost:9000/image/1.jpg
     * 若访问当前upload路径,实际路径为: http://localhost:9000/image/upload
     * 获取application.properties中文件声明的数据
     */
    @Value("${knows.resource.path}")
    private File resourcePath;

    @Value("${knows.resource.host}")
    private String resourceHost;

    @PostMapping
    public String uploadFile(MultipartFile imageFile) throws IOException {
        //按日期创建文件夹
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        //path: 2021/08/02
        File folder = new File(resourcePath,path);
        //folder : E:/upload/2021/08/02
        folder.mkdirs();//创建文件
        String fileName = imageFile.getOriginalFilename();//处理文件名,UUID生成文件名+原文件名后缀
        String ext = fileName.substring(fileName.lastIndexOf("."));//截取文件扩展名
        String name = UUID.randomUUID().toString()+ext; //生成新的名称
        File file = new File(folder,name);//创建文件上传的最终位置 E:/upload/2021/12/10上传name.ext
        imageFile.transferTo(file);//执行上传
        String url = resourceHost+"/"+path+"/"+name;//拼接上传成功的文件的访问路径并返回
        log.debug("访问路径为:{}",url);
        return url;
    }
}
