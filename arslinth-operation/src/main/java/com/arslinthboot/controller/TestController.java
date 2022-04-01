package com.arslinthboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arslinth
 * @ClassName TestController
 * @Description
 * @Date 2022/3/5
 */
@RestController
@RequestMapping("/testController")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("returnFalse")
    public String hello() {
        return "hello";
    }

    public boolean returnFalse(){
        System.out.println("111");

        return false;
    }
}
