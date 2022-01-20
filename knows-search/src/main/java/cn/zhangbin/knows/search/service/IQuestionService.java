package cn.zhangbin.knows.search.service;

import cn.zhangbin.knows.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;

public interface IQuestionService {
    //sync同步的缩写
    public void sync();

    //根据当前用户,查询条件,分页查询问题
    PageInfo<QuestionVo> search(String username,String key,Integer pageNum,Integer pageSize);
}
