package com.team3.workoutplanservice.dto.request;

import java.time.LocalDate;

public record WeeklyRequestDTO (

        Long userId,
        LocalDate startDate,
        LocalDate endDate
) {}