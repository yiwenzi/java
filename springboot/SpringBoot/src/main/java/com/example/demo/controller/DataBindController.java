package com.example.demo.controller;

import com.example.demo.domain.Admin;
import com.example.demo.domain.User;
import com.example.demo.domain.UserListForm;
import com.example.demo.domain.UserSetForm;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by hunter on 2018/4/2.
 */
@RestController
@RequestMapping("/data")
public class DataBindController {

    @RequestMapping(value = "/baseType.do")
    public String baseType (HttpServletRequest request,@RequestParam(value = "age", required = false)int age) {
        Map<String,String[]> map = request.getParameterMap();
        System.out.println(request.getRequestURI());
        System.out.println("=======");
        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
        System.out.println(request.getPathInfo());
        System.out.println(request.getPathInfo());
        return "age: " + age;
    }

    @RequestMapping(value = "/baseType2.do")
    public String baseType2(Integer age) {
        return "age: " + age;
    }

    //TODO http://localhost:8080/data/array.do?name=joker&name=hunter&name=harry
    @RequestMapping(value = "/array.do")
    public String array(String[] name) {
        StringBuilder sbf = new StringBuilder();
        for (String s : name) {
            sbf.append(s).append(" ");
        }
        return sbf.toString();
    }

    @RequestMapping(value = "/common")
    public String common(User user, Admin admin) {
        return user + ", " + admin;
    }

    @InitBinder("user")
    public void initUser(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    @InitBinder("admin")
    public void initAdmin(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("admin.");
    }

    @RequestMapping(value = "list")
    public String getUserList(UserListForm userListForm) {
        return userListForm.toString();
    }

    @RequestMapping(value = "set")
    public String getUserSet(UserSetForm userSetForm) {
        return userSetForm.toString();
    }
}
