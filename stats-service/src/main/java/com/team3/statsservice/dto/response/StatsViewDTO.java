package com.team3.statsservice.dto.response;

import com.team3.statsservice.domian.Stats;

import java.time.LocalDate;

public record StatsViewDTO (
        LocalDate startDate,
        LocalDate endDate,
        Integer totalDuration,
        Integer totalCalories
) {

    public static StatsViewDTO fromEntity(Stats stats) {
        return new StatsViewDTO (
                stats.getStartDate(),
                stats.getEndDate(),
                stats.getTotalDuration(),
                stats.getTotalCalories()
        );
    }
}
