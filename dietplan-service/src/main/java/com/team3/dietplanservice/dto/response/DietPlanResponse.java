package com.team3.dietplanservice.dto.response;

public record DietPlanResponse (
        Long dietPlanId,
        String mealTime,
        Long foodId,
        Integer calories,
        String foodName

) {}