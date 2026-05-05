package com.saccoplus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDashboardResponse {

    private double currentBalance;
    private double totalDeposits;
    private double totalWithdrawals;
    private List<RecentTransaction> recentTransactions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentTransaction {
        private String type;
        private double amount;
        private String status;
        private LocalDateTime date;
    }
}
