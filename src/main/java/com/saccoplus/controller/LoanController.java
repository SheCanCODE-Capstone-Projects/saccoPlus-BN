package com.saccoplus.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saccoplus.dto.request.ApplyLoanRequest;
import com.saccoplus.dto.request.RepaymentRequest;
import com.saccoplus.dto.request.ReviewLoanRequest;
import com.saccoplus.dto.response.LoanResponse;
import com.saccoplus.entity.Installment;
import com.saccoplus.entity.Loan;
import com.saccoplus.service.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanResponse> apply(
            @RequestBody ApplyLoanRequest request) {

        return ResponseEntity.ok(
                loanService.apply(request));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Loan>> pending() {

        return ResponseEntity.ok(
                loanService.getPendingLoans());
    }

    @PostMapping("/{loanId}/approve")
    public ResponseEntity<LoanResponse> approve(
            @PathVariable Long loanId,
            @RequestBody ReviewLoanRequest request) {

        return ResponseEntity.ok(
                loanService.approve(loanId, request));
    }

    @PostMapping("/{loanId}/reject")
    public ResponseEntity<LoanResponse> reject(
            @PathVariable Long loanId,
            @RequestBody ReviewLoanRequest request) {

        return ResponseEntity.ok(
                loanService.reject(loanId, request));
    }

    @PostMapping("/{loanId}/info")
    public ResponseEntity<LoanResponse> info(
            @PathVariable Long loanId,
            @RequestBody ReviewLoanRequest request) {

        return ResponseEntity.ok(
                loanService.requestMoreInfo(
                        loanId,
                        request));
    }

    @PostMapping("/{loanId}/disburse")
    public ResponseEntity<LoanResponse> disburse(
            @PathVariable Long loanId) {

        return ResponseEntity.ok(
                loanService.disburse(loanId));
    }

    @PostMapping("/repay")
    public ResponseEntity<LoanResponse> repay(
            @RequestBody RepaymentRequest request) {

        return ResponseEntity.ok(
                loanService.repay(request));
    }

    @GetMapping("/{loanId}/schedule")
    public ResponseEntity<List<Installment>> schedule(
            @PathVariable Long loanId) {

        return ResponseEntity.ok(
                loanService.getSchedule(loanId));
    }
}