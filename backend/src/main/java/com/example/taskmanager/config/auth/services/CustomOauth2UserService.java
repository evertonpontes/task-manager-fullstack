package com.example.taskmanager.config.auth.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("github".equals(registrationId)) {
            String token = userRequest.getAccessToken().getTokenValue();
            String email = fetchGitHubPrimaryEmail(token);
            Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
            if (email != null) {
                attributes.put("email", email);
            }
            return new DefaultOAuth2User(
                    oAuth2User.getAuthorities(),
                    attributes,
                    "id"
            );
        }

        return oAuth2User;
    }

    private String fetchGitHubPrimaryEmail(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.github.com/user/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> emails = response.getBody();
        if (emails == null) return null;

        return emails.stream()
                .filter(email -> Boolean.TRUE.equals(email.get("primary")) && Boolean.TRUE.equals(email.get("verified")))
                .map(email -> (String) email.get("email"))
                .findFirst()
                .orElse(null);
    }

}
