package com.bigant.gaeme.config.interceptor;

import com.bigant.gaeme.annotation.Authorized;
import com.bigant.gaeme.annotation.Authorized.AuthRole;
import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    private final JwtBuilder jwtBuilder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Authorized authorized = handlerMethod.getMethodAnnotation(Authorized.class);

        if (authorized == null) {
            return true;
        }


        if (authorized.authRole() == AuthRole.MEMBER) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (token == null || token.length() < 7) {
                throw new AuthenticationServiceException("토큰 형식 오류");
            }

            Long userId = jwtBuilder.decryptJwt(token.substring(7));

            return userRepository.existsById(userId);
        }

        return true ;
    }

}
