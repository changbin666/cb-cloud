package com.cb.cloud.gateway.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.nimbusds.jose.JWSObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    /**
     * 判断当前token是否具有当前路径访问权限，如果有则放行，如没有则返回false
     * @param mono
     * @param authorizationContext
     * @return
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String requestPath = exchange.getRequest().getURI().getPath();
        System.out.println("当前请求路径："+ requestPath);

        String authorizationToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("当前请求头Authorization中的值:"+authorizationToken);
        if (StringUtils.isBlank(authorizationToken)) {
            System.out.println("当前请求头Authorization中的值不存在");
            return Mono.just(new AuthorizationDecision(false));
        }

        String token = authorizationToken.replace("Bearer ", "");
        if (StrUtil.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }
        //解析token，获得当前用户信息
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String userStr = jwsObject.getPayload().toString();
        System.out.println("当前用户的userStr："+userStr);
        //通过sql获取当前用户的所有角色

        JSONObject userJsonObject = new JSONObject(userStr);

        /*//通过角色获取该角色所能访问的全部URL，我这里写死了
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("/demo1/get/1"));
        authorities.add(new SimpleGrantedAuthority("/demo2/testfeign"));

        //遍历全部URL集合，与当前request的path进行比
        for (GrantedAuthority authority : authorities) {
            String authorityStr= authority.getAuthority();
            if (authorityStr.equals(requestPath)) {
                //把解析出来的用户信息，存放到请求头里面
                ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
                exchange = exchange.mutate().request(request).build();
                return Mono.just(new AuthorizationDecision(true));
            }
        }*/

        //模拟的，不校验当前用户是否有当前菜单的访问权限，直接过。如果要校验，请启用上面的代码
        // 把解析出来的用户信息，存放到请求头里面
        ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
        exchange = exchange.mutate().request(request).build();
        return Mono.just(new AuthorizationDecision(true));
    }

}
