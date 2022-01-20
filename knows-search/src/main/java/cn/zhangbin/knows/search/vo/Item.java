package cn.zhangbin.knows.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
@AllArgsConstructor //全参构造
@NoArgsConstructor //无参构造
//spring data Es的注解,indexName指定操作的索引名称,如果索引不存在,自动创建
@Document(indexName = "items")
public class Item {
    @Id
    private Long id;//type = FieldType.Text是字符串,支持分词操作
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String title;//type = FieldType.Keyword是字符串,不支持分词操作
    @Field(type = FieldType.Keyword)
    private String category;//分类
    @Field(type = FieldType.Keyword)
    private String brand; //品牌
    @Field(type = FieldType.Double)
    private Double price; //价格
    @Field(type = FieldType.Keyword,index = false)//type = FieldType.Keyword, index = false表示当前属性不创建索引库,一般作用在不会称为查询条件的属性上,例如图片地址
    private String image;
}
