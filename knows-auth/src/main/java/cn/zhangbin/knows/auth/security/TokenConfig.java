package cn.zhangbin.knows.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    //解密口令(变量名和值为随意的)
    private final String SIGNING_KEY="auth";

    //SpringBoot生成令牌需要一个令牌生成器对象,Oauth2使用这个令牌生成器生成令牌
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }

    //声明并向Spring容器保存一个JWT转换器
    //这个转换器能够将任何信息转为JWT令牌
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //设置解密口令
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
}