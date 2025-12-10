package com.team3.statsservice.dto.request;

public record CreateStatsDTO (

        Long userId,
        Integer totalDuration,
        Integer totalCalories
) {}
