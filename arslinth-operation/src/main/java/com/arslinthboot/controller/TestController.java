package com.arslinthboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arslinth
 * @ClassName TestController
 * @Description TODO
 * @Date 2022/3/5
 */
@RestController
@RequestMapping("/testController")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
