package com.team3.statsservice.dto.response;

public record CalorieRankingDto (

        Long userId,
        int calorieRank,
        Integer totalCalories
) {}
