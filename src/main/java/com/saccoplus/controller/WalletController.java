package com.saccoplus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saccoplus.dto.request.DepositRequest;
import com.saccoplus.dto.request.WithdrawalRequest;
import com.saccoplus.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest request) {
        walletService.deposit(request);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawalRequest request) {
        walletService.withdraw(request);
        return ResponseEntity.ok("Withdrawal successful");
    }

}
