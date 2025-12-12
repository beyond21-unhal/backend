package com.team3.statsservice.dto.response;

import java.time.LocalDate;

public record WeeklyStatsResponseDTO(

        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalDuration,
        Integer totalCalories
) {}
