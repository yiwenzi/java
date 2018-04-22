package com.imooc.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hunter on 2018/4/17.
 */
@RestController
public class ServerController {
    @GetMapping("/msg")
    public String msg() {
        return "this is product's msg";
    }
}
