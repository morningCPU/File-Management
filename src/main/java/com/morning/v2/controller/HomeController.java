package com.morning.v2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 首页控制器，处理根路径请求
 */
@Controller
public class HomeController {

    /**
     * 处理根路径请求，重定向到登录页面
     * @return 重定向到登录页面的视图
     */
    @GetMapping("/")
    public RedirectView home() {
        // 重定向到登录页面
        return new RedirectView("/login.html");
    }
}
