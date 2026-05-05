package com.saccoplus.controller;

import com.saccoplus.dto.response.SavingsDashboardResponse;
import com.saccoplus.service.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<SavingsDashboardResponse> getDashboard(@PathVariable Long userId) {
        return ResponseEntity.ok(savingsService.getDashboard(userId));
    }
}
