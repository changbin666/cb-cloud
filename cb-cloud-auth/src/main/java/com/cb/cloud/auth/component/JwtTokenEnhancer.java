package com.cb.cloud.auth.component;

import com.cb.cloud.auth.entity.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器
 * 将用户的ID，手机号，姓名存放在token中
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("userName", securityUser.getUsername());
        info.put("userID",securityUser.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}