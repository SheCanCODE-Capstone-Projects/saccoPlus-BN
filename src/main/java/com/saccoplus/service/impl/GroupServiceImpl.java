package com.saccoplus.service.impl;

import com.saccoplus.dto.request.MemberDto;
import com.saccoplus.dto.request.RegisterGroupRequest;
import com.saccoplus.entity.*;
import com.saccoplus.exception.BusinessException;
import com.saccoplus.repository.*;
import com.saccoplus.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final IndividualUserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerGroup(RegisterGroupRequest request) {

        if (request.getMembers() == null || request.getMembers().size() < 2) {
            throw new BusinessException("Group requires at least 2 members", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByPhone(request.getRepresentativePhone())) {
            throw new BusinessException("Phone already registered", HttpStatus.CONFLICT);
        }

        IndividualUser representative = IndividualUser.builder()
                .firstName(request.getRepresentativeFirstName())
                .lastName(request.getRepresentativeLastName())
                .phone(request.getRepresentativePhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();

        userRepository.save(representative);

        // ✅ Fixed: BigDecimal.ZERO and lowercase indUser
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .indUser(representative)
                .build();

        walletRepository.save(wallet);

        Group group = Group.builder()
                .groupName(request.getGroupName())
                .representative(representative)
                .createdAt(LocalDateTime.now())
                .build();

        groupRepository.save(group);

        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(representative)
                        .joinedAt(LocalDateTime.now())
                        .build()
        );

        for (MemberDto dto : request.getMembers()) {

            if (userRepository.existsByPhone(dto.getPhone())) {
                throw new BusinessException(
                        "Member phone already registered: " + dto.getPhone(),
                        HttpStatus.CONFLICT
                );
            }

            IndividualUser member = IndividualUser.builder()
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .phone(dto.getPhone())
                    .password(passwordEncoder.encode("default123"))
                    .role(Role.MEMBER)
                    .build();

            userRepository.save(member);

            groupMemberRepository.save(
                    GroupMember.builder()
                            .group(group)
                            .user(member)
                            .joinedAt(LocalDateTime.now())
                            .build()
            );
        }
    }
}