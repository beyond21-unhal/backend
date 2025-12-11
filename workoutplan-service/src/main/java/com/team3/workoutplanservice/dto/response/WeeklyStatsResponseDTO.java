package com.team3.workoutplanservice.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public record WeeklyStatsResponseDTO(

        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalDuration,
        Integer totalCalories
) {

    @Builder
    public WeeklyStatsResponseDTO(Long userId, LocalDate startDate, LocalDate endDate, Integer totalDuration, Integer totalCalories) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDuration = totalDuration;
        this.totalCalories = totalCalories;
    }
}