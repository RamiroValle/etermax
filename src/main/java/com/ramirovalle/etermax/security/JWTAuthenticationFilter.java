package com.ramirovalle.etermax.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        // no esta implementada una autenticacion por usuario
        // cualquier user/pass devuelve un token
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Basic ")) {
            return null;
        }
        byte[] userBytes = Base64.getDecoder().decode(auth.replace("Basic ", ""));
        String[] user= new String(userBytes, StandardCharsets.UTF_8).split(":");
        return new UsernamePasswordAuthenticationToken(user[0], user[1], new ArrayList<>());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = Jwts.builder()
                .setSubject((String) auth.getPrincipal())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.validityInMilliseconds))
                .signWith(SecurityConfig.secretKey)
                .compact();

        res.getWriter().write(token);
        res.getWriter().flush();
    }
}
