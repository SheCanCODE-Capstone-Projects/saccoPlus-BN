package com.saccoplus.service.impl;

import com.saccoplus.dto.response.TransactionResponse;
import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.Transaction;
import com.saccoplus.exception.BusinessException;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.repository.TransactionRepository;
import com.saccoplus.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final IndividualUserRepository userRepository;

    @Override
    public Page<TransactionResponse> getUserTransactions(
            Long userId,
            int page,
            int size,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        // 1. Find user
        IndividualUser user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException("User not found", HttpStatus.NOT_FOUND)
                );

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Transaction> transactions;

        // 2. Filter or not
        if (startDate != null && endDate != null) {

            // Optional validation
            if (startDate.isAfter(endDate)) {
                throw new BusinessException("Invalid date range", HttpStatus.BAD_REQUEST);
            }

            transactions = transactionRepository.findByUserAndCreatedAtBetween(
                    user, startDate, endDate, pageable
            );

        } else {
            transactions = transactionRepository.findByUser(user, pageable);
        }

        // 3. Map to response
        return transactions.map(tx -> new TransactionResponse(
                "Transaction fetched successfully",   // message
                tx.getAmount(),                       // amount
                0.0,                                  // temporary balance
                tx.getStatus(),                       // status
                tx.getCreatedAt()                     // date
        ));
    }
}