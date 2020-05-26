package com.example.crawlertest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private TokenStore tokenStore;

    @Autowired
    public ResourceServerConfig(AccessTokenConverter accessTokenConverter, TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources //
                .resourceId("am-applicatie-resource-id") //
                .stateless(true) //
                .tokenStore(tokenStore) //
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/crawl/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
