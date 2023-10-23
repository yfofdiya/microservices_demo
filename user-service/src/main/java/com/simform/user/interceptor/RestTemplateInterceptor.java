package com.simform.user.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@Component
@Slf4j
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private OAuth2AuthorizedClientManager clientManager;

    public RestTemplateInterceptor(OAuth2AuthorizedClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = clientManager
                .authorize(
                        OAuth2AuthorizeRequest
                                .withClientRegistrationId("my-client")
                                .principal("internal")
                                .build()
                )
                .getAccessToken()
                .getTokenValue();
        log.info("Rest Template interceptor: Token :  {} ", token);
        request.getHeaders().add("Authorization", "Bearer " + token);
        return execution.execute(request, body);
    }
}
