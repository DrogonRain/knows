package cn.zhangbin.knows.portal.controller;


import cn.zhangbin.knows.portal.model.Tag;
import cn.zhangbin.knows.portal.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    private ITagService tagService;

    //获取所有标签集合的控制方法,@GetMapping("")表示当前控制器请求路径为类上定义的路径
    //即 localhost:8080/v1/tags
    @GetMapping("")
    public List<Tag> tags(){
        List<Tag> tags = tagService.getTags();
        return tags;
    }
}
