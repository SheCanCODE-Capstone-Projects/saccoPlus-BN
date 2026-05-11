package com.saccoplus.controller;

import com.saccoplus.dto.request.ForgotPasswordRequest;
import com.saccoplus.dto.request.ResetPasswordRequest;
import com.saccoplus.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordResetService.sendResetLink(request.getEmail());
        return ResponseEntity.ok("Reset link sent to " + request.getEmail());
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}