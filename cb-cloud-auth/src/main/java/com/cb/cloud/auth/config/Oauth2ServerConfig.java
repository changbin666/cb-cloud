package com.cb.cloud.auth.config;

import com.cb.cloud.auth.common.OAuth2Properties;
import com.cb.cloud.auth.component.JwtTokenEnhancer;
import com.cb.cloud.auth.service.OAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证服务器配置
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String ENV_OAUTH = "authentication.oauth";
    private final OAuth2UserService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenEnhancer jwtTokenEnhancer;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

  /*  @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }*/

  @Bean
  public TokenStore jwtStore() {
      TokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
      return tokenStore;
  }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String idForEncode = "bcrypt";
        Map<String,PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);

        //通过jdbc读数据库的client的配置，和以下方式配置结果一样，只是以下方式在内存中
               /* .inMemory()
                .withClient("rajithapp")
                .resourceIds("resource")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .authorities("ROLE_ADMIN","ROLE_USER")
                .secret("secret")
                .and().withClient("zxyyapp")
                .resourceIds("resource")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("read", "write")
                .authorities("ROLE_ADMIN","ROLE_USER")
                .secret("secret");*/
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
        endpoints.tokenStore(jwtStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //配置加载用户信息的服务
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }
}
