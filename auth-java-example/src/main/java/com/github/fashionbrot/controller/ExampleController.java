package com.github.fashionbrot.controller;

import com.github.fashionbrot.Token;
import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.service.ExampleService;
import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ExampleController {


    final ExampleService exampleService;

    @ResponseBody
    @RequestMapping("/login")
    public String login(){
        Token token=new Token();
        token.setUserId(1);
        token.setIssuedAt(new Date());
        token.setExpiresAt(DateUtil.addDays(1));
        return AuthUtil.encrypt(exampleService.getAlgorithm(),token);
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
