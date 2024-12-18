package org.example.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
@RestController
public class ClientRestController {
    private final WebClient webClient;

    @GetMapping("/auth-code")
    Mono<String> useOauthWithAuthCode() {
        Mono<String> retrievedResource = webClient.get()
                .uri("http://localhost:9090/secure")
                .retrieve()
                .bodyToMono(String.class);
        return retrievedResource.map(string ->
                "We retrieved the following resource using Oauth: " + string);
    }

    @GetMapping("/hi")
    public Mono<String> getTest() {
        return Mono.just("Hi");
    }

    @GetMapping("/token")
    public Mono<String> getToken(Authentication authentication) {
        String jwtToken = "";
        if (authentication instanceof OAuth2AuthenticationToken token && token.getPrincipal() instanceof DefaultOidcUser user) {
            OidcIdToken idToken = user.getIdToken();
            jwtToken = idToken.getTokenValue();
        }

        log.info("jwt token -> " + jwtToken);
        final String tkn = jwtToken;
        return webClient.get()
                .uri("http://localhost:9090/secure")
                .headers(h -> h.setBearerAuth(tkn))
                .retrieve()
                .bodyToMono(String.class);
    }
}