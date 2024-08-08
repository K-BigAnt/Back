package com.bigant.gaeme.controller;

import com.bigant.gaeme.repository.enums.AuthCorp;
import com.bigant.gaeme.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{auth_corp}")
    public void authorize(
            @PathVariable("auth_corp") AuthCorp authCorp,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletResponse response
    ) {
        redirectToAuthPage(response, oauthService.getAuthUri(redirectUri, authCorp));
    }

    private void redirectToAuthPage(HttpServletResponse response, String authUri) {
        try {
            response.sendRedirect(authUri);
        } catch (Exception e) {
            throw new IllegalStateException("OAuth 인증에 실패했습니다.", e);
        }
    }

}
