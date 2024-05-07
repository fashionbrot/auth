package com.github.fashionbrot.controller;

import com.github.fashionbrot.service.ExampleService;
import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.MapUtil;
import com.github.fashionbrot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ExampleController {


    final ExampleService exampleService;

    @ResponseBody
    @RequestMapping("/login")
    public String login(){
        return JwtUtil.encrypt(exampleService.getAlgorithm(),30*60, MapUtil.createMap("userId",1));
    }


    @Permission(value = {"test1"})
    @ResponseBody
    @RequestMapping("/test1")
    public String test1(){
        return "success";
    }

    @Permission(value = {"test2"})
    @ResponseBody
    @RequestMapping("/test2")
    public String test2(){
        return "success";
    }

}
