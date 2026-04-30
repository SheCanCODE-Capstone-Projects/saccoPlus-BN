package com.saccoplus.controller;

import com.saccoplus.dto.request.RegisterGroupRequest;
import com.saccoplus.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/register")
    public ResponseEntity<String> registerGroup(@RequestBody RegisterGroupRequest request) {
        groupService.registerGroup(request);
        return ResponseEntity.ok("Group registered successfully");
    }
}