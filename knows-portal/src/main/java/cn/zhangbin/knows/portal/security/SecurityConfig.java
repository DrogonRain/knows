package cn.zhangbin.knows.portal.security;

import cn.zhangbin.knows.portal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//启动Spring Security的权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
//WebSecurityConfigurerAdapter类是适配器,在配置的时候需要写配置类进行继承而后编写自己所需的特殊配置
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //首先从Spring容器中获得编写的用户名和密码登录的对象
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
       //这个方法就是Spring Security提供的方法,他会获得编写代码返回的UserDetails对象进行登录验证
        auth.userDetailsService(userDetailsService);
    }

    //通过重写下面的方法设置指定资源无需登录即可访问
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //设置请求限制
            .antMatchers(//"/index_student.html", //指定受影响资源
                    "/img/**",
                    "/js/*",
                    "/css/*",
                    "/bower_components/**",
                    "/login.html",
                    "/register.html",
                    "/register").permitAll() //全部允许,不登录就能访问
            .anyRequest()//其他请求
            .authenticated() //需要登录才能访问
            .and() //上面设置完开始设置其他内容
            .formLogin() //使用表单登录
            .loginPage("/login.html") //自定义登录页面
            .loginProcessingUrl("/login") //设置登录提交路径
            .failureUrl("/login.html?error") //设置登录失败跳转页面
            .defaultSuccessUrl("/index.html") //设置登录成功后默认跳转
            .and() //登录设置完毕
            .logout() //开始设置登出
            .logoutUrl("/logout") //登出默认访问这个路径
            .logoutSuccessUrl("/login.html?logout")//登出之后跳转到登录页
            .and()
            .csrf()
            .disable(); //关闭防跨域攻击
    }
}
