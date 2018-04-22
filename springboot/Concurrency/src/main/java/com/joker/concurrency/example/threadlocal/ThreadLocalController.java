package com.joker.concurrency.example.threadlocal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hunter on 2018/4/9.
 */
@RestController
@RequestMapping("/threadLocal")
public class ThreadLocalController {
    @RequestMapping("/test")
    public Long test() {
        return RequestHolder.getId();
    }
}
