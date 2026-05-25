package com.saccoplus.service.impl;

import org.springframework.stereotype.Service;

import com.saccoplus.dto.request.DepositRequest;
import com.saccoplus.dto.request.WithdrawalRequest;
import com.saccoplus.dto.response.TransactionResponse;
import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.Transaction;
import com.saccoplus.entity.TransactionStatus;
import com.saccoplus.entity.TransactionType;
import com.saccoplus.entity.Wallet;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.repository.TransactionRepository;
import com.saccoplus.service.WalletService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    // ✅ Fixed: must be final for @RequiredArgsConstructor to inject them
    private final IndividualUserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse withdraw(WithdrawalRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        // ✅ BigDecimal comparison
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // ✅ BigDecimal subtraction
        wallet.setBalance(wallet.getBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setUser(user);

        transactionRepository.save(transaction);
        userRepository.save(user);

        return new TransactionResponse(
                "Withdrawal successful",
                request.getAmount(),
                wallet.getBalance().doubleValue(),
                transaction.getStatus());
    }

    @Override
    public TransactionResponse deposit(DepositRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());


        wallet.setBalance(wallet.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setUser(user);

        transactionRepository.save(transaction);
        userRepository.save(user);

        return new TransactionResponse(
                "Deposit successful",
                request.getAmount(),
                wallet.getBalance().doubleValue(),
                transaction.getStatus());
    }
}