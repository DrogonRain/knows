package cn.zhangbin.knows.search;

import cn.zhangbin.knows.commons.model.Question;
import cn.zhangbin.knows.search.repository.ItemRepository;
import cn.zhangbin.knows.search.repository.QuestionRepository;
import cn.zhangbin.knows.search.service.IQuestionService;
import cn.zhangbin.knows.search.vo.Item;
import cn.zhangbin.knows.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class KnowsSearchApplicationTests {

    @Resource
    ElasticsearchOperations elasticsearchOperations;

    @Test
    void contextLoads() {
        System.out.println(elasticsearchOperations);
    }

    @Autowired
    ItemRepository itemRepository;
    //新增数据
    @Test
    void add(){
        Item item = new Item(1L,"罗技激光无线游戏鼠标","外设","罗技",188.0,"/image/1.jpg");
        itemRepository.save(item);
        System.out.println("ok");
    }

    @Test
    void getId(){
        Optional<Item> item = itemRepository.findById(1L);
        System.out.println(item);
    }

    //批量加入数据
    @Test
    void addList(){
        List<Item> list=new ArrayList<>();
        list.add(new Item(2L,"华为无线蓝牙降噪耳机",
                "耳机","华为"
                ,468.0,"/image/2.jpg"));
        list.add(new Item(3L,"苹果无线蓝牙音乐耳机",
                "耳机","苹果"
                ,799.0,"/image/3.jpg"));
        list.add(new Item(4L,"罗技有线机械竞技键盘",
                "外设","罗技"
                ,389.0,"/image/4.jpg"));
        list.add(new Item(5L,"罗技无线蓝牙游戏耳机",
                "耳机","罗技"
                ,628.0,"/image/5.jpg"));
        list.add(new Item(6L,"华为蓝牙电动牙刷",
                "牙刷","华为"
                ,666.0,"/image/6.jpg"));
        itemRepository.saveAll(list);
        System.out.println("ok");
    }

    //全查
    @Test
    void getAll() {
        Iterable<Item> items = itemRepository.findAll();
        //Iterable是List的父接口
        //支持迭代器遍历
        for(Item item:items){
            System.out.println(item);
        }
    }

    @Resource
    IQuestionService questionService;
    @Test
    void testData(){
        questionService.sync();
        System.out.println("done");
    }

    @Resource
    QuestionRepository questionRepository;
    //全查
    @Test
    void all(){
        Iterable<QuestionVo> qs = questionRepository.findAll();
        qs.forEach(questionVo -> System.out.println(questionVo));
    }

    @Test
    void searchKeyWord(){
        Page<QuestionVo> qs = questionRepository.queryAllByParams("java","java",11, PageRequest.of(0,8));
        qs.forEach(questionVo -> System.out.println(questionVo));
    }

    @Test
    void service(){
        PageInfo<QuestionVo> pageInfo = questionService.search("st2","java",1,8);
        pageInfo.getList().forEach(questionVo -> System.out.println(questionVo));
    }
}
