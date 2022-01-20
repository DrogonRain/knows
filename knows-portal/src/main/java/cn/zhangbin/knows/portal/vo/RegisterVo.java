package cn.zhangbin.knows.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterVo {

    /**
     * @NotBlank:只能验证String类型的属性,验证字符串不能为null不能为"",而且会先对当前字符串对象调用tirm()方法后再验证是不是""
     * @Pattern:设置正则表达式的验证
     * @NotNull:可以验证任何类型的属性,规定当前属性不能为null
     * @NotEmpty:一般使用在数组或集合上,规定当前数组或集合不能为null并且长度不能为0
     */

    //@NotBlank 规定当前inviteCode属性不能为空,message是当inviteCode属性为空时返回的错误信息
    @NotBlank(message = "邀请码不能为空!")
    private String inviteCode; //邀请码
    @NotBlank(message = "手机号不能为空!")
    //@Pattern表示满足regexp后面的正则表达式,以及message定义不满足时返回的错误信息
    @Pattern(regexp = "^1\\d{10}$" , message = "手机号格式不正确!")
    private String phone; //手机号\用户名
    @NotBlank(message = "昵称不能为空!")
    @Pattern(regexp = "^.{2,20}$", message = "昵称是2到20个字符")
    private String nickname; //昵称
    @NotBlank(message = "密码不能为空!")
    @Pattern(regexp = "^\\w{6,20}$", message = "密码是6~20位字符")
    private String password; //密码
    @NotBlank(message = "确认密码不能为空!")
    private String confirm; //确认密码
}
