package cn.zhangbin.knows.search.interceptor;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //jwt(JSON WEB TOKEN)令牌会在用户请求中,我们需要在当前拦截器中获得JWT令牌
        String token = request.getParameter("accessToken");
        //借助授权服务器提供的解析JWT的方法获得用户详情对象
        String url = "http://auth-service/oauth/check_token?token={1}";
        //通过map接收获取到的用户信息
        Map<String,Object> map = restTemplate.getForObject(url,Map.class,token);
        //根据key获取不同的参数,注意:默认情况下map.get("authorities")被解析为一个List,作用为获取用户所有权限
        List<String> list = (List<String>) map.get("authorities");
        //UserDetails赋值时需要的是String类型的数组,所以将list转为数组
        String[] auth = list.toArray(new String[0]);
        //构建UserDetails对象
        UserDetails userDetails = User.builder()
                .username(map.get("user_name").toString())
                .password("")
                .authorities(auth)
                .build();
        //将用户详情保存到SpringSecurity中,使得控制器方法参数@AuthenticationPrincipal可以获取用户详情
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails,userDetails.getPassword(), AuthorityUtils.createAuthorityList(auth));
        //将解析的用户详情和当前请求相关联,关联后才能在控制器中获取用户详情
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        //将authenticationToken赋值到SpringSecurity容器中
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);
        //返回true
        return true;
    }
}
