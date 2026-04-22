package com.saccoplus.service.impl;

import org.hibernate.tool.schema.internal.IndividuallySchemaMigratorImpl;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saccoplus.dto.request.RegisterRequest;
import com.saccoplus.entity.IndividualUser;
import com.saccoplus.entity.Role;
import com.saccoplus.entity.Wallet;
import com.saccoplus.repository.IndividualUserRepository;
import com.saccoplus.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private  IndividualUserRepository individualUserRepository;
    private PasswordEncoder passwordEncoder;
    

    @Override
    public void registerIndividual(RegisterRequest request) {
        if (individualUserRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        IndividualUser user = IndividualUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();
               

        

        Wallet wallet = Wallet.builder()
                .balance(0)
                .IndUser(user)
                .build();
        
        user.setWallet(wallet);

        individualUserRepository.save(user);
        
    }

}
    

   



