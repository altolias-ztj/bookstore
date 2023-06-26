package com.bookstore.user.controller;

import cn.hutool.http.HttpRequest;
import com.bookstore.common.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping("/wx")
public class WxController {

    @Autowired
    private WxUtil wxUtil;

    @GetMapping("/test")
    public Object origin(String signature, String timestamp, String nonce, String echostr) {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        return null;
    }

    @GetMapping(value = "/core")
    public Object core(HttpServletRequest request) {
        try {
            ServletInputStream is = request.getInputStream();
            Object resp = wxUtil.getResponse(is);
            return resp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
