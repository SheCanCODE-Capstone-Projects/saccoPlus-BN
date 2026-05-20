package com.saccoplus.dto.response;

import com.saccoplus.entity.LoanStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {

    private String message;

    private double amount;

    private double remainingBalance;

    private LoanStatus status;
}
