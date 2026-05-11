package com.saccoplus.service.impl;

import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.PasswordResetToken;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.repository.PasswordResetTokenRepository;
import com.saccoplus.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final IndividualUserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendResetLink(String email) {

        //  Check if user exists
        IndividualUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        //  Delete any existing token for this email
        tokenRepository.deleteByEmail(email);

        //  Generate a unique token
        String token = UUID.randomUUID().toString();

        //  Save token with 1 hour expiry
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        //  Send email with reset link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("SACCOPlus — Password Reset Request");
        message.setText(
                "Hello " + user.getFirstName() + ",\n\n" +
                        "You requested to reset your password.\n\n" +
                        "Click the link below to reset your password:\n" +
                        "http://localhost:3000/auth/reset-password?token=" + token + "\n\n" +
                        "This link expires in 1 hour.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "SACCOPlus Rwanda"
        );

        mailSender.send(message);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        //  Find the token in database
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset link"));

        // Check if token is expired
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Reset link has expired");
        }

        //  Check if token was already used
        if (resetToken.isUsed()) {
            throw new RuntimeException("Reset link has already been used");
        }

        //  Find the user and update password
        IndividualUser user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}