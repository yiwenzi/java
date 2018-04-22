package com.example.demo.controller;

import com.example.demo.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hunter on 2017/10/23.
 */
@RestController
@RequestMapping("/hello")
//@Controller
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    //可以映射多个
    //@RequestMapping(value = "/say", method = RequestMethod.GET)
    //@RequestMapping(value = {"/hello","/hi"}, method = RequestMethod.GET)
    @GetMapping("/say")
    public String say() {
        return girlProperties.getCupSize();
        //return "index";
    }
}
