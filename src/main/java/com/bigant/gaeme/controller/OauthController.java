package com.bigant.gaeme.controller;

import com.bigant.gaeme.dao.dto.AuthResponseDto;
import com.bigant.gaeme.repository.enums.AuthCorp;
import com.bigant.gaeme.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{auth_corp}")
    public void authorize(
            @PathVariable("auth_corp") AuthCorp authCorp,
            HttpServletResponse response
    ) {
        redirectToAuthPage(response, oauthService.getAuthUri(authCorp));
    }

    private void redirectToAuthPage(HttpServletResponse response, String authUri) {
        try {
            response.sendRedirect(authUri);
        } catch (Exception e) {
            throw new IllegalStateException("OAuth 인증에 실패했습니다.", e);
        }
    }

    @PostMapping("/{auth_corp}")
    public AuthResponseDto signInOrSignUp(
            HttpServletResponse response,
            @PathVariable("auth_corp") AuthCorp authCorp,
            @RequestParam String code
    ) {
        AuthResponseDto authResponseDto = oauthService.signInOrSignUp(code, authCorp);
        Cookie cookie = new Cookie("token", authResponseDto.getToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.ofDays(7L).getSeconds());
//        cookie.setSecure(true); // https 에서만 쿠키 전송
        response.addCookie(cookie);
        return authResponseDto;
    }

}
