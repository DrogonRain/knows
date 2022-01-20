package cn.zhangbin.knows.auth.security;

import cn.zhangbin.knows.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
//下面注解表示当前类是配置Oauth2的核心配置类,Spring Oauth2框架会在需要时调用这个配置类中的各个方法和偶对象
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;//管理权限
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * 配置权限服务器端点信息
     * 主要是当用户访问到/oauth/token时我们的操作
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置SpringSecurity中获得的认证管理器
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)//配置获取用户详情的方法
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)//为了安全,限制登录提交的方式只能是Post
                .tokenServices(tokenService());//配置令牌生成器
    }

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    //配置令牌生成
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices services = new DefaultTokenServices();//创建保存token容器
        services.setSupportRefreshToken(true);//设置service支持令牌刷新策略
        services.setTokenStore(tokenStore);//设置令牌生成策略
        TokenEnhancerChain chain = new TokenEnhancerChain();//设置龙牌增强对象(Jwt令牌固定配置)
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter));//将Jwt令牌转换器添加至这个对象中
        services.setTokenEnhancer(chain);//设置chain到当前生成令牌的对象
        services.setAccessTokenValiditySeconds(3600);//设置令牌有效期为3600秒,即一小时之内有效
        services.setRefreshTokenValiditySeconds(3600*72);//设置令牌刷新最大时间为72小时
        services.setClientDetailsService(clientDetailsService);//配置客户端详情
        return services;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    //配置客户端详情(规定客户端权限)
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("knows")//定义客户端id
                .secret(passwordEncoder.encode("779"))//客户端密钥
                .scopes("all")//授权客户端权限:all只是个名字,类似ROLE_STUDENT
                .authorizedGrantTypes("password","refresh_token");//当前客户端支持Oauth2操作,password表示Oauth2支持用户名密码登录,refresh_token表示Oauth2支持令牌刷新
    }

    //认证成功的安全策略设置(登录成功后允许在资源服务器中做的事情)
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")//允许任何人访问/oauth/token_key端点
                .checkTokenAccess("permitAll()")//允许任何人访问/oauth/check_token端点
                .allowFormAuthenticationForClients();//允许客户端进行表单权限认证(如果登录成功赋予令牌)
    }
}
