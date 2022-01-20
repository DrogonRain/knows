package cn.zhangbin.knows.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //编写SpringSecurity全部放行
    //因为授权方式为了Oauth2令牌方式,原有的Session验证就不需要了

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and().formLogin();
    }

    /**
     * 我们需要将一个加密对象保存到Spring容器
     * 框架中需要加密的组件会自动获取它进行加密操作
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 我们集成的父类WebSecurityConfigurerAdapter
     * 这个父类中有一个Oauth2框架需要的对象,我们需要把它保存到Spring容器
     * 否则运行报错
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}