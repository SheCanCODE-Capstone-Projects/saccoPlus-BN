package com.saccoplus.service;

import com.saccoplus.dto.request.LoginRequest;
import com.saccoplus.dto.request.RegisterRequest;
import com.saccoplus.dto.response.AuthResponse;
import com.saccoplus.dto.response.UserProfileResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    UserProfileResponse getCurrentUser(String email);
}
