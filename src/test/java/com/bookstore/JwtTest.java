package com.bookstore;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

@SpringBootTest
public class JwtTest {
    //生成一个JWT
    @Test
    public void test1() {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + 5 * 60 * 1000);
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(expireAt);
        builder.withClaim("id", 1);
        builder.withClaim("name", "admin");
        String token = builder.sign(Algorithm.HMAC256("woniu"));
        System.out.println(token);
    }

    //验证token
    @Test
    public void test2() {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("woniu")).build();
        DecodedJWT verify = verifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiYWRtaW4iLCJpZCI6MSwiZXhwIjoxNjg1NjczNTY2fQ.CXK3We-k3mQgOf-Bg4lLoT4uw9duZ4EL8k3m4qc94nU");
        Map<String, Claim> claims = verify.getClaims();
        claims.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }
}
