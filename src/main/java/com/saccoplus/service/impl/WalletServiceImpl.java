package com.saccoplus.service.impl;

import org.springframework.stereotype.Service;

import com.saccoplus.dto.request.WithdrawalRequest;
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

     private  IndividualUserRepository userRepository;
    private  TransactionRepository transactionRepository;

    @Override
    public void withdraw(WithdrawalRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();

        if (wallet.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setUser(user);

        wallet.setBalance(wallet.getBalance() - request.getAmount());

        transaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.save(transaction);
        userRepository.save(user);
    }



}
