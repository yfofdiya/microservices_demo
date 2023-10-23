package com.simform.user.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private OAuth2AuthorizedClientManager clientManager;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = clientManager
                .authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("my-client")
                        .principal("internal")
                        .build()
                )
                .getAccessToken()
                .getTokenValue();
        log.info("Feign Client interceptor: Token :  {} ", token);
        requestTemplate.header("Authorization", "Bearer " + token);
    }
}
