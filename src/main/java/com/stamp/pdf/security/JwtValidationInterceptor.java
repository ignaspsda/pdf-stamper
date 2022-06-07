package com.stamp.pdf.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stamp.pdf.exception.JwtBearerException;
import com.stamp.pdf.exception.JwtExpiredException;
import com.stamp.pdf.exception.NoJwtProvidedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = request.getHeader("Authorization");
        String token;

        if (jwtToken == null){
            throw new NoJwtProvidedException();
        }

        if (jwtToken.startsWith("Bearer ")) {
            token = jwtToken.substring(7, jwtToken.length());
        } else {
            throw new JwtBearerException();
        }

        DecodedJWT jwt = JWT.decode(token);
        if (jwt.getExpiresAt().before(new Date())) {
            throw new JwtExpiredException();
        }
        return true;
    }
}
