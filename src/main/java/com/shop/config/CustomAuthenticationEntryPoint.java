package com.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            // 로그인 되지 않은 사용자가(인증이 되지 않은 사용자) 인증이 필요한 url에 접속을 시도하면, 아래에 지정해준 주소로 redirect 되는데, 그 redirect 주소가 permitAll()에 포함되어 있어야 정상적으로 redirect 된다.
            // 만약, Role이 USER인 사용자가 (로그인 인증은 됨), ADMIN만 접근 가능한 url에 접근을 시도하면, 아래 설정한 주소로 redirect 되지 않고, 403 whitelabel error page가 뜬다.
            // 만약 아래의 주소가 permitAll()에 포함되어 있지 않다면, too many redirection 에러가 브라우저에 나타난다.
            response.sendRedirect("/members/login");
        }
    }

}
