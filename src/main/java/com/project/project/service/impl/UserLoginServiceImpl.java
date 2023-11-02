package com.project.project.service.impl;

import com.project.project.model.UserLoginLogout;
import com.project.project.repository.UserLoginLogoutRepository;
import com.project.project.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginLogoutRepository userLoginLogoutRepository;

    @Override
    public List<UserLoginLogout> getAll(){
        return userLoginLogoutRepository.findAll();
    }
}
