package com.saccoplus.service;

import com.saccoplus.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;

public interface TransactionService {

    Page<TransactionResponse> getUserTransactions(
            Long userId,
            int page,
            int size,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
