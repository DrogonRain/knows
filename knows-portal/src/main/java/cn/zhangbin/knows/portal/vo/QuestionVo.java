package cn.zhangbin.knows.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
//@Accessors(chain = true):当前对象支持链式set赋值
@Accessors(chain = true)
public class QuestionVo implements Serializable {

    @NotBlank(message = "标题不能为空!")
    @Pattern(regexp = "^.{3,50}$",message = "标题是3到50个字符")
    private String title;//问题的标题
    @NotEmpty(message = "至少选择一个标签!")
    private String[] tagNames={}; //用户选中的所有标签
    @NotEmpty(message = "至少选择一个讲师!")
    private String[] teacherNames={}; //用户选中讲师的昵称
    @NotBlank(message = "内容不能为空!")
    private String content; //问题的内容
}
