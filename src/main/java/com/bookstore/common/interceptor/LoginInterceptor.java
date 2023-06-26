package com.bookstore.common.interceptor;

import com.bookstore.common.entity.User;
import com.bookstore.admin.service.UserService;
import com.bookstore.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

//@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        PrintWriter out = response.getWriter();
        if (token == null || token.equals("")) {
            out.print("{'code':405,'message':'warning'}");
            out.close();
            return false;
        }
        try {
            Map<String, Long> map = jwtUtil.verify(token);
            int code = map.get("code").intValue();
            switch (code) {
                case 1:
                    out.print("{'code':200,'message':'success'}");
                    break;
                case 2:
                    out.print("{'code':200,'message':'delay'}");
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
            long userId = map.get("userId");
            User user = userService.getByUserId(userId);
            request.getSession().setAttribute("user", user);
        } catch (Exception e) {
            out.print("{'code':405,'message':'warning'}");
            out.close();
            return false;
        }
        return true;
    }
}
