package com.team3.dietplanservice.dto.response;

import com.team3.dietplanservice.domain.DietPlan;
import lombok.Builder;

public record DietPlanResponse (
        Long dietPlanId,
        String mealTime,
        Long foodId,
        Integer calories,
        String foodName,
        String imageUrl

) {

    @Builder
    public DietPlanResponse {
    }

    public static DietPlanResponse from(DietPlan dietPlan) {
        return DietPlanResponse.builder()
                .dietPlanId(dietPlan.getDietPlanId())
                .mealTime(dietPlan.getMealTime())
                .foodId(dietPlan.getFoodId())
                .calories(dietPlan.getCalories())
                .foodName(dietPlan.getFoodName())
                .imageUrl(dietPlan.getImageUrl())
                .build();
    }
}