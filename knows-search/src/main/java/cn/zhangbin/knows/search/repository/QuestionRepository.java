package cn.zhangbin.knows.search.repository;

import cn.zhangbin.knows.search.vo.QuestionVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionRepository extends ElasticsearchRepository<QuestionVo,Integer> {
    /**
     * must：必须的，就是并且关系
     *
     * should：应该的，就是或者关系
     *
     * match：内容匹配
     *
     * term：完全相等
     *
     * bool：布尔,套在 must\should外面
     */
    @Query("{\n" +
            "    \"bool\": {\n" +
            "      \"must\": [{\n" +
            "        \"bool\": {\n" +
            "          \"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"content\": \"?1\"}}]\n" +
            "        }\n" +
            "      }, {\n" +
            "        \"bool\": {\n" +
            "          \"should\": [{\"term\": {\"publicStatus\": 1}}, {\"term\": {\"userId\": ?2}}]\n" +
            "        }\n" +
            "      }]\n" +
            "    }\n" +
            "  }")
    Page<QuestionVo> queryAllByParams(String title,String content,Integer userId,Pageable pageable);
}
