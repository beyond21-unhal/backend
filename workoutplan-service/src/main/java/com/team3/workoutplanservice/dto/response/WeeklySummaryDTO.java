package com.team3.workoutplanservice.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public record WeeklySummaryDTO (
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Integer plannedAmount,
        Integer achievedAmount
) {
    @Builder
    public WeeklySummaryDTO(Long userId, LocalDate startDate, LocalDate endDate, Integer plannedAmount, Integer achievedAmount) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plannedAmount = plannedAmount;
        this.achievedAmount = achievedAmount;
    }
}