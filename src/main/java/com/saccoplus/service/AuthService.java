package com.saccoplus.service;

import com.saccoplus.dto.request.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
}
