package com.saccoplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saccoplus.entity.Installment;
import com.saccoplus.entity.Loan;

@Repository
public interface InstallmentRepository
        extends JpaRepository<Installment, Long> {

    List<Installment> findByLoan(Loan loan);
}
