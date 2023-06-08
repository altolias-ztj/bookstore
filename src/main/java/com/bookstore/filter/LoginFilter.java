package com.bookstore.filter;

import com.bookstore.entity.User;
import com.bookstore.entity.Permission;
import com.bookstore.service.UserService;
import com.bookstore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LoginFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        User user = null;
        //验证token
        Map<String, Long> map = jwtUtil.verify(token);
        switch (map.get("code").intValue()) {
            //正常
            case 1:
                long id = map.get("userId");
                user = userService.getByUserId(id);
                request.getSession().setAttribute("user", user);
                break;
            //可以续期
            case 2:
                //请求正常进行
                long id1 = map.get("userId");
                user = userService.getByUserId(id1);
                request.getSession().setAttribute("user", user);
                //获取一个新的token
                String newToken = jwtUtil.getToken(user);
                //把这个token给响应头
                response.setHeader("newToken", newToken);
                break;
            /*//过期，不续期
            case 3:
                PrintWriter out = response.getWriter();
                response.setContentType("application/json;charset=utf-8");
                out.print("{\"code\":406,\"message\":\"token expired\"}");
                out.close();
                return;
            //不合法
            case 4:
                PrintWriter out1 = response.getWriter();
                out1.print("{\"code\":405,\"message\":\"FBI WARNING\"}");
                out1.close();
                return;*/
        }
        //权限的集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        //给权限
        if (map.get("code") == 1 || map.get("code") == 2) {
            List<Permission> permissions = user.getRole().getPermissions();
            permissions.forEach(a -> {
                authorities.add(new SimpleGrantedAuthority(a.getPermission()));
            });
        }
        //封闭到UsernamePasswordAuthenticationToken中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, authorities);
        //交给spring security
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
