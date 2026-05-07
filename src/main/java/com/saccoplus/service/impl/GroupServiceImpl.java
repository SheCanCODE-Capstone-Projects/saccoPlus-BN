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

        // Validate members
        if (request.getMembers() == null || request.getMembers().size() < 2) {
            throw new BusinessException("Group requires at least 2 members", HttpStatus.BAD_REQUEST);
        }

        // Check representative phone uniqueness
        if (userRepository.existsByPhone(request.getRepresentativePhone())) {
            throw new BusinessException("Phone already registered", HttpStatus.CONFLICT);
        }

        // Create representative
        IndividualUser representative = IndividualUser.builder()
                .firstName(request.getRepresentativeFirstName())
                .lastName(request.getRepresentativeLastName())
                .phone(request.getRepresentativePhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();

        userRepository.save(representative);

        // Create wallet
        Wallet wallet = Wallet.builder()
                .balance(0.0)
                .IndUser(representative)
                .build();

        walletRepository.save(wallet);

        // Create group
        Group group = Group.builder()
                .groupName(request.getGroupName())
                .representative(representative)
                .createdAt(LocalDateTime.now())
                .build();

        groupRepository.save(group);

        // Add representative as member
        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(representative)
                        .joinedAt(LocalDateTime.now())
                        .build()
        );

        //Add other members
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