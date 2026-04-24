package com.saccoplus.repository;

import com.saccoplus.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByIndUserId(Long userId);

}
