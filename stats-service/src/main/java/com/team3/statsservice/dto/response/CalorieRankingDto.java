package com.team3.statsservice.dto.response;

public record CalorieRankingDto (

        int calorieRank,
        Long userId,
        Integer totalCalories
) {}
