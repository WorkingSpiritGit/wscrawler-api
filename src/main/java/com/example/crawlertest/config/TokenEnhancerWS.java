package com.example.crawlertest.config;

import com.example.crawlertest.domein.GebruikerInlog;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

class TokenEnhancerWS implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        GebruikerInlog principal = (GebruikerInlog) oAuth2Authentication.getPrincipal();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("voornaam", principal.getVoornaam());
        extraClaims.put("tussenvoegsel", principal.getTussenvoegsel());
        extraClaims.put("achternaam", principal.getAchternaam());
        extraClaims.put("displaynaam", principal.getDisplaynaam());
        extraClaims.put("gebruiker_id", principal.getGebruiker_id());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(extraClaims);
        return oAuth2AccessToken;
    }
}
