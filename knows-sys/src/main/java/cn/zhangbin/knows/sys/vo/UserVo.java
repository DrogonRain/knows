package cn.zhangbin.knows.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserVo implements Serializable {

    private Integer id;
    private String username;
    private String nickname;
    //提问的问题数量
    private Integer questions;
    // 收藏数
    private Integer collections;
    // 回答数
    private Integer answers;
}
