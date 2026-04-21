package com.saccoplus.service.impl;

import org.springframework.stereotype.Service;

import com.saccoplus.dto.request.RegisterRequest;
import com.saccoplus.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public String register(RegisterRequest request){
        return "User registered successfully";
    }

}
