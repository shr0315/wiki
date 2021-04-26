package com.jiawa.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //返回字符串
//@Controller  返回页面
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
