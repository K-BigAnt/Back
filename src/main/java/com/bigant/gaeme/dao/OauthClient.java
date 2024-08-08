package com.bigant.gaeme.dao;

import java.net.URI;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OauthClient {

    private final String baseUrl;

    private final String clientId;

    private final String authPath;

    private final String scope;

    public URI getAuthUri(String redirectUri) {
        String uriString = String.format("%s?client_id=%s&response_type=%s&redirect_uri=%s&scope=%s",
                this.baseUrl + this.authPath, this.clientId, "code", redirectUri, this.scope);

        return URI.create(uriString);
    }

}
