package com.saccoplus.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyLoanRequest {

    private Long userId;

    private double amount;

    private int duration;

    private String documents;
}
