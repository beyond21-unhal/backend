package com.team3.reportservice.dto.response;

import java.time.LocalDate;

public record WeeklySummaryDto (
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Integer plannedAmount,
        Integer achievedAmount
) {}
