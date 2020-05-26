package com.example.crawlertest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = (int) TimeUnit.HOURS.toSeconds(1);
    private static final int REFRESH_TOKEN_VALIDITY_SECONDS = (int) TimeUnit.HOURS.toSeconds(6);

    @Value("${ama.oauth.client.id}")
    private String oauthClientId;
    @Value("${ama.oauth.client.secret}")
    private String oauthClientSecret;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private JwtAccessTokenConverter accessTokenConverter;
    private TokenStore tokenStore;
    private TokenEnhancer amTokenEnhancer;

    @Autowired
    public AuthorizationServerConfig(@Qualifier("authenticationManagerBean") final AuthenticationManager authenticationManager,
                                     final PasswordEncoder encoder, final JwtAccessTokenConverter accessTokenConverter,
                                     final TokenStore tokenStore, TokenEnhancer amTokenEnhancer) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenStore = tokenStore;
        this.amTokenEnhancer = amTokenEnhancer;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security //
                .tokenKeyAccess("permitAll()") //
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients //
                .inMemory().withClient(oauthClientId).secret(encoder.encode(oauthClientSecret)) //
                .scopes(SCOPE_READ, SCOPE_WRITE) //
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, GRANT_TYPE_REFRESH_TOKEN) //
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS) //
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);//
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(amTokenEnhancer, accessTokenConverter));

        endpoints //
                .tokenStore(tokenStore) //
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager) //
        ;
    }
}
