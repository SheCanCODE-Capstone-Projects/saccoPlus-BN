package com.saccoplus.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int installmentNumber;

    private LocalDate dueDate;

    private double principal;

    private double interest;

    private double totalAmount;

    private double remainingBalance;

    @Enumerated(EnumType.STRING)
    private Paymentstatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
