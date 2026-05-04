package com.saccoplus.service;

import com.saccoplus.dto.request.WithdrawalRequest;

public interface WalletService {

    public void withdraw(WithdrawalRequest request);
}
