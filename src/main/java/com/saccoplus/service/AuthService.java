package com.saccoplus.service;

import com.saccoplus.dto.ForgotPasswordRequest;
import com.saccoplus.entity.OtpEntity;
import com.saccoplus.entity.User;
import com.saccoplus.repository.OtpRepository;
import com.saccoplus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       OtpRepository otpRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpEntity otpEntity = OtpEntity.builder()
                .userId(user.getId())
                .code(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        otpRepository.save(otpEntity);

        emailService.sendOtp(user.getEmail(), otp);
    }
}