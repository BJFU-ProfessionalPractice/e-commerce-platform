package com.example.demo.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShopActionController {

    @RequestMapping("/testwsl")
    public void testwsl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String str = "{\"code\":0,\"msg\":\"\",\"count\":" + 1 + ",\"data\":" + "111" + "}";
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(str);
    }

}
