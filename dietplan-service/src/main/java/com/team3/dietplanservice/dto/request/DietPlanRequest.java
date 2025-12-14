package com.team3.dietplanservice.dto.request;


import com.team3.dietplanservice.domain.DietPlan;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record DietPlanRequest(

         @NotBlank(message = "음식 종류를 선택해주세요.")
         String foodName,

         @NotNull(message = "음식 칼로리를 입력해주세요")
         @Positive(message = "칼로리는 0보다 큰 값만 가능합니다.")
         @Min(value = 1, message = "칼로리는 0일 순 없습니다")
         Integer calories,

         @NotNull(message = "음식 ID를 입력해주세요")
         Long foodId,

         @NotBlank(message = "식사시간을 선택해주세요")
         String mealTime,

         @NotNull(message = "날짜를 입력해주세요")
         LocalDate date



) {
    public DietPlan toEntity(Long userId) {
        return DietPlan.builder()
                .userId(userId)
                .mealTime(mealTime)
                .foodId(foodId)
                .foodName(foodName)
                .calories(calories)
                .date(date)
                .imageUrl(null)
                .build();
    }
}