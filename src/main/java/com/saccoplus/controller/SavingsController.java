package com.saccoplus.controller;

import com.saccoplus.dto.request.DepositRequest;
import com.saccoplus.dto.request.WithdrawalRequest;
import com.saccoplus.dto.response.TransactionResponse;
import com.saccoplus.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request));
    }
}