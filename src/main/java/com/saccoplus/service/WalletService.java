package com.saccoplus.service;

import com.saccoplus.dto.request.DepositRequest;
import com.saccoplus.dto.request.WithdrawalRequest;
import com.saccoplus.dto.response.TransactionResponse;

public interface WalletService {

    TransactionResponse withdraw(WithdrawalRequest request);

    TransactionResponse deposit(DepositRequest request);
}
