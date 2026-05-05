package com.saccoplus.service.impl;

import com.saccoplus.dto.response.SavingsDashboardResponse;
import com.saccoplus.dto.response.SavingsDashboardResponse.RecentTransaction;
import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.TransactionType;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.repository.TransactionRepository;
import com.saccoplus.service.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {

    private final IndividualUserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public SavingsDashboardResponse getDashboard(Long userId) {
        IndividualUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double currentBalance = user.getWallet().getBalance();

        double totalDeposits = transactionRepository.findByUserAndType(user, TransactionType.DEPOSIT)
                .stream().mapToDouble(t -> t.getAmount()).sum();

        double totalWithdrawals = transactionRepository.findByUserAndType(user, TransactionType.WITHDRAWAL)
                .stream().mapToDouble(t -> t.getAmount()).sum();

        List<RecentTransaction> recent = transactionRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .limit(5)
                .map(t -> new RecentTransaction(
                        t.getType().name(),
                        t.getAmount(),
                        t.getStatus().name(),
                        t.getCreatedAt()))
                .collect(Collectors.toList());

        return new SavingsDashboardResponse(currentBalance, totalDeposits, totalWithdrawals, recent);
    }
}
