package com.eb.disputemanagement.dispute.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private final OAuth2AuthorizedClientService clientService;

    public EmployeeFeignClientInterceptor(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationToken authenticationToken=(OAuth2AuthenticationToken)authentication;
//        OAuth2AuthorizedClient client=clientService.loadAuthorizedClient(
//                authenticationToken.getAuthorizedClientRegistrationId(),
//                authenticationToken.getName()
//        );
//        OAuth2AccessToken auth2AccessToken=client.getAccessToken();
        if ((authentication instanceof JwtAuthenticationToken)) {
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, authenticationToken.getToken().getTokenValue()));
        }
    }
}
