package com.ander.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Date 2018/11/24 14:59.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "jsp/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "jsp/home";
    }


    @RequestMapping("/fiveZeroZero")
    public String fiveZeroZero() {
        return "jsp/500";
    }

    @RequestMapping("/fourZeroFour")
    public String fourZeroFour() {
        return "jsp/404";
    }
}
