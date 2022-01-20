package cn.zhangbin.knows.search.repository;

import cn.zhangbin.knows.search.vo.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    //按title属性的分词索引结果进行查询
    //Matches就是模糊查询的命令,意思为匹配,自动查询分词索引
    Iterable<Item> queryItemsByTitleMatches(String title);

    //查询两个条件的
    Iterable<Item> queryItemsByTitleMatchesAndCategoryMatches(String title,String category);

    //排序查询
    Iterable<Item> queryItemsByTitleMatchesOrderByPriceAsc(String title);

    //分页查询的返回值Page等价于PageHelper框架中的PageInfo类型
    //也就是说Page类中不但包含查询结果,还包含分页信息
    Page<Item> queryItemsByTitleMatchesOrderByPriceAsc(String title, Pageable pageable);
}
