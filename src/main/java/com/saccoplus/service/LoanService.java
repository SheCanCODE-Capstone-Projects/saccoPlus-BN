package com.saccoplus.service;

import java.util.List;

import com.saccoplus.dto.request.ApplyLoanRequest;
import com.saccoplus.dto.request.RepaymentRequest;
import com.saccoplus.dto.request.ReviewLoanRequest;
import com.saccoplus.dto.response.LoanResponse;
import com.saccoplus.entity.Installment;
import com.saccoplus.entity.Loan;

public interface LoanService {

    LoanResponse apply(ApplyLoanRequest request);

    LoanResponse approve(
            Long loanId,
            ReviewLoanRequest request);

    LoanResponse reject(
            Long loanId,
            ReviewLoanRequest request);

    LoanResponse requestMoreInfo(
            Long loanId,
            ReviewLoanRequest request);

    LoanResponse disburse(Long loanId);

    LoanResponse repay(RepaymentRequest request);

    List<Installment> getSchedule(Long loanId);

    List<Loan> getPendingLoans();
}
