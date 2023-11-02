package com.project.project.controller;

import com.project.project.model.UserLoginLogout;
import com.project.project.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userloginlogout")
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;

    

    @GetMapping("/getall")
    public List<UserLoginLogout> getAll(){
        return userLoginService.getAll();
    }
}
