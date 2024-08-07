package com.bigant.gaeme.dao;

import java.net.URI;

import com.bigant.gaeme.dao.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class OauthClient {

    private final String clientId;

    private final String clientSecret;

    private final String scope;

    private final String authUrl;

    private final String tokenUrl;

    private final String infoUrl;

    private final RestClient restClient = RestClient.builder().build();

    public URI getAuthUri(String redirectUri) {
        String uri = String.format("%s?client_id=%s&response_type=%s&redirect_uri=%s&scope=%s",
                authUrl, this.clientId, "code", redirectUri, this.scope);
        return URI.create(uri);
    }

    public String getAccessToken(String authCode, String redirectUri) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", authCode);

        ResponseEntity<TokenResponseDto> response = restClient.post().uri(tokenUrl)
                .body(requestBody)
                .retrieve()
                .toEntity(TokenResponseDto.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("액세스 토큰을 가져오는데 실패했습니다.");
        }

        return response.getBody().getAccessToken();
    }

    public String getUserInfo(String accessToken) {
        ResponseEntity<String> response = restClient.get().uri(infoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(String.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("유저 정보를 가져오는데 실패했습니다.");
        }

        return response.getBody();
    }

}
