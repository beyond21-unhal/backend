package com.team3.workoutplanservice.dto.request;

import java.time.LocalDate;

public record WeeklyStatsDTO (

        Long userId,
        LocalDate startDate,
        LocalDate endDate
) {}