package com.saccoplus.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RegisterGroupRequest {

    private String groupName;

    private String representativeFirstName;
    private String representativeLastName;
    private String representativePhone;
    private String password;
    private List<MemberDto> members;
}
