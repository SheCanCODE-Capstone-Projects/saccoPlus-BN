package com.saccoplus.dto.response;

import com.saccoplus.entity.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String message;
    private double amount;
    private double balance;
    private TransactionStatus status;

}
