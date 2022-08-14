package com.sparta.sorisam.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아온다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인한다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장한다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
                throw new JwtException("유요하지 않은 토큰");
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
                throw new JwtException("토큰 기한 만료");
            } catch(SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
                throw new JwtException("사용자 인증 실패");
            }
        }
        chain.doFilter(request, response);
    }

}
