package com.saccoplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.Loan;
import com.saccoplus.entity.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    boolean existsByUserAndStatus(
            IndividualUser user,
            LoanStatus status);
}
