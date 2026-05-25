package com.saccoplus.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import com.saccoplus.dto.request.ApplyLoanRequest;
import com.saccoplus.dto.request.RepaymentRequest;
import com.saccoplus.dto.request.ReviewLoanRequest;
import com.saccoplus.dto.response.EligibilityResponse;
import com.saccoplus.dto.response.LoanResponse;
import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.Installment;
import com.saccoplus.entity.Loan;
import com.saccoplus.entity.LoanStatus;
import com.saccoplus.entity.Paymentstatus;
import com.saccoplus.entity.Wallet;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.repository.InstallmentRepository;
import com.saccoplus.repository.LoanRepository;
import com.saccoplus.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final IndividualUserRepository userRepository;

    private static final BigDecimal RATIO = new BigDecimal("0.5");

    private EligibilityResponse checkEligibility(
            IndividualUser user,
            double requestedAmount) {

        Wallet wallet = user.getWallet();

        if (wallet == null) {
            return new EligibilityResponse(false, "Wallet not found");
        }

        BigDecimal savings = wallet.getBalance();
        BigDecimal requested = BigDecimal.valueOf(requestedAmount);

        if (savings.compareTo(requested.multiply(RATIO)) < 0) {
            return new EligibilityResponse(false, "Savings below eligibility threshold");
        }

        boolean hasDefaultedLoan = loanRepository.existsByUserAndStatus(
                user, LoanStatus.DEFAULTED);

        if (hasDefaultedLoan) {
            return new EligibilityResponse(false, "User has defaulted loan");
        }

        return new EligibilityResponse(true, "Eligible");
    }

    @Override
    public LoanResponse apply(ApplyLoanRequest request) {

        IndividualUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        EligibilityResponse eligibility = checkEligibility(user, request.getAmount());

        Loan loan = new Loan();
        loan.setAmount(request.getAmount());
        loan.setDuration(request.getDuration());
        loan.setDocuments(request.getDocuments());
        loan.setRemainingBalance(request.getAmount());
        loan.setInterestRate(10);
        loan.setUser(user);

        if (eligibility.isEligible()) {
            loan.setStatus(LoanStatus.PENDING);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
        }

        loanRepository.save(loan);

        return new LoanResponse(
                eligibility.isEligible()
                        ? "Loan application submitted"
                        : "Loan rejected: " + eligibility.getReason(),
                loan.getAmount(),
                loan.getRemainingBalance(),
                loan.getStatus());
    }

    @Override
    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatus(LoanStatus.PENDING);
    }

    @Override
    public LoanResponse approve(Long loanId, ReviewLoanRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.APPROVED);
        loan.setOfficerComment(request.getComment());
        loanRepository.save(loan);
        return new LoanResponse("Loan approved", loan.getAmount(),
                loan.getRemainingBalance(), loan.getStatus());
    }

    @Override
    public LoanResponse reject(Long loanId, ReviewLoanRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.REJECTED);
        loan.setOfficerComment(request.getComment());
        loanRepository.save(loan);
        return new LoanResponse("Loan rejected", loan.getAmount(),
                loan.getRemainingBalance(), loan.getStatus());
    }

    @Override
    public LoanResponse requestMoreInfo(Long loanId, ReviewLoanRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.INFO_REQUIRED);
        loan.setOfficerComment(request.getComment());
        loanRepository.save(loan);
        return new LoanResponse("Additional information requested",
                loan.getAmount(), loan.getRemainingBalance(), loan.getStatus());
    }

    @Override
    public LoanResponse disburse(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Loan must be approved first");
        }

        IndividualUser user = loan.getUser();
        Wallet wallet = user.getWallet();

        // ✅ BigDecimal addition
        wallet.setBalance(
                wallet.getBalance().add(BigDecimal.valueOf(loan.getAmount())));

        generateSchedule(loan);
        loan.setStatus(LoanStatus.DISBURSED);
        loanRepository.save(loan);

        return new LoanResponse("Loan disbursed successfully",
                loan.getAmount(), loan.getRemainingBalance(), loan.getStatus());
    }

    private void generateSchedule(Loan loan) {

        double monthlyPrincipal = loan.getAmount() / loan.getDuration();
        double balance = loan.getAmount();

        for (int i = 1; i <= loan.getDuration(); i++) {
            double interest = balance * (loan.getInterestRate() / 100.0) / 12;
            double total = monthlyPrincipal + interest;
            balance -= monthlyPrincipal;

            Installment installment = new Installment();
            installment.setInstallmentNumber(i);
            installment.setDueDate(LocalDate.now().plusMonths(i));
            installment.setPrincipal(monthlyPrincipal);
            installment.setInterest(interest);
            installment.setTotalAmount(total);
            installment.setRemainingBalance(balance);
            installment.setPaymentStatus(Paymentstatus.UNPAID);
            installment.setLoan(loan);
            installmentRepository.save(installment);
        }
    }

    @Override
    public LoanResponse repay(RepaymentRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        IndividualUser user = loan.getUser();
        Wallet wallet = user.getWallet();

        BigDecimal repayAmount = BigDecimal.valueOf(request.getAmount());


        if (wallet.getBalance().compareTo(repayAmount) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }


        wallet.setBalance(wallet.getBalance().subtract(repayAmount));

        double remaining = loan.getRemainingBalance() - request.getAmount();
        loan.setRemainingBalance(remaining);

        if (remaining <= 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        } else {
            loan.setStatus(LoanStatus.ACTIVE);
        }

        loanRepository.save(loan);

        return new LoanResponse("Repayment successful",
                loan.getAmount(), loan.getRemainingBalance(), loan.getStatus());
    }

    @Override
    public List<Installment> getSchedule(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        return installmentRepository.findByLoan(loan);
    }
}