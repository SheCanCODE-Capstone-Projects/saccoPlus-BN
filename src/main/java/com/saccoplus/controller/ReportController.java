package com.saccoplus.controller;

import com.saccoplus.entity.Loan;
import com.saccoplus.entity.Transaction;
import com.saccoplus.entity.User;
import com.saccoplus.repository.LoanRepository;
import com.saccoplus.repository.TransactionRepository;
import com.saccoplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;


    @GetMapping("/admin-summary")
    public ResponseEntity<Map<String, Object>> getAdminSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", userRepository.count());
        summary.put("totalLoans", loanRepository.count());
        summary.put("totalTransactions", transactionRepository.count());
        return ResponseEntity.ok(summary);
    }


    @GetMapping("/member-statement/{userId}")
    public ResponseEntity<Map<String, Object>> getMemberStatement(
            @PathVariable Long userId) {
        Map<String, Object> statement = new HashMap<>();
        List<Transaction> transactions = transactionRepository
                .findByUserId(userId);
        statement.put("userId", userId);
        statement.put("transactions", transactions);
        statement.put("totalTransactions", transactions.size());
        return ResponseEntity.ok(statement);
    }

    @GetMapping("/loan-report")
    public ResponseEntity<List<Loan>> getLoanReport() {
        return ResponseEntity.ok(loanRepository.findAll());
    }
}