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

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private IndividualUserRepository userRepository;
    private TransactionRepository transactionRepository;

    @Override
    public TransactionResponse withdraw(WithdrawalRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();

        if (wallet.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        double updatedBalance = wallet.getBalance() - request.getAmount();
        wallet.setBalance(updatedBalance);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setUser(user);

        wallet.setBalance(wallet.getBalance() - request.getAmount());

        transaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.save(transaction);
        userRepository.save(user);

        return new TransactionResponse(
                "Withdrawal successful",
                request.getAmount(),
                wallet.getBalance(),
                transaction.getStatus());
    }

    @Override
    public TransactionResponse deposit(DepositRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();

        double updatedBalance = wallet.getBalance() + request.getAmount();

        wallet.setBalance(updatedBalance);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        userRepository.save(user);

        return new TransactionResponse("Deposit successful", request.getAmount(), wallet.getBalance(),
                transaction.getStatus());
    }

}
