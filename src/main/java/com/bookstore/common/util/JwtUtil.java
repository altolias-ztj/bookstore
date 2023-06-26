package com.bookstore.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.bookstore.common.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${woniu.token.expire}")
    private int expire;

    @Value("${woniu.token.canContinue}")
    private int canContinue;

    @Value("${woniu.token.secret}")
    private String secret;

    public String getToken(User user) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expire * 60 * 1000);
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(expireAt);
        builder.withClaim("userId", user.getUserId());
        builder.withClaim("userName", user.getUserName());
        String token = builder.sign(Algorithm.HMAC256(secret));
        return token;
    }

    public Map<String, Long> verify(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        Map<String, Long> map = new HashMap<>();
        try {
            DecodedJWT verify = verifier.verify(token);
            Map<String, Claim> claims = verify.getClaims();
            claims.forEach((k, v) -> {
                map.put(k, v.asLong());
            });
            map.put("code", 1L);
        } catch (TokenExpiredException e) {
            String subToken = token.substring(token.indexOf(".") + 1, token.lastIndexOf("."));
            String json = new String(Base64.getDecoder().decode(subToken));
            JsonMapper jsonMapper = JsonMapper.builder().build();
            try {
                Map m = jsonMapper.readValue(json, Map.class);
                Long exp = Long.valueOf(m.get("exp").toString());
                Long max = exp * 1000 + canContinue * 60 * 1000;
                if (max > System.currentTimeMillis()) {
                    map.put("userId",Long.valueOf(m.get("userId").toString()));
                    map.put("code", 2L);
                } else {
                    map.put("code", 3L);
                }
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            map.put("code", 4L);
        }
        return map;
    }
}
