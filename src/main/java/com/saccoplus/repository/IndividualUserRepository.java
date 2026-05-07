package com.saccoplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saccoplus.entity.IndividualUser;

public interface IndividualUserRepository extends JpaRepository<IndividualUser, Long> {

    boolean existsByPhone(String phone);

}
