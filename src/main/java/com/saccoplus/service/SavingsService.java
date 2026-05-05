package com.saccoplus.service;

import com.saccoplus.dto.response.SavingsDashboardResponse;

public interface SavingsService {

    SavingsDashboardResponse getDashboard(Long userId);
}
