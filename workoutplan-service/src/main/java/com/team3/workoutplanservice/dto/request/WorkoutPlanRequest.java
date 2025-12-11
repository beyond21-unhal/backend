package com.team3.workoutplanservice.dto.request;

import com.team3.workoutplanservice.domain.WorkoutPlan;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkoutPlanRequest (


    @NotBlank(message="주 차를 입력해 주세요")
    String dayOfWeek,

    @NotBlank(message="운동 종류를 선택해주세요")
    String exerciseType,

    @NotBlank(message="운동 카테고리를 선택해주세요")
    String category,

    @NotBlank(message="운동 난이도를 선택해주세요")
    String difficulty,

    @NotBlank(message="어떤 부위를 운동하시나요?")
    String bodyPart,

    @NotBlank(message="날짜를 입력해주세요")
    LocalDate date,

    @NotNull(message = "운동시간을 입력해주세요")
    @Min(value = 1, message = "운동시간은 0일 순 없습니다")
    Integer workoutRecord,

    @NotNull(message = "소모 칼로리를 입력해주세요")
    @Min(value = 1, message = "칼로리는 0일 순 없습니다")
    Integer burnedCalories,

    @NotNull(message = "달성 여부를 입력해주세요")
    Boolean isCompleted

) {
        public WorkoutPlan toEntity(Long userId) {
            return WorkoutPlan.builder()
                    .userId(userId)
                    .dayOfWeek(dayOfWeek)
                    .exerciseType(exerciseType)
                    .category(category)
                    .difficulty(difficulty)
                    .bodyPart(bodyPart)
                    .date(date)
                    .workoutRecord(workoutRecord)
                    .burnedCalories(burnedCalories)
                    .isCompleted(isCompleted)
                    .build();
        }
}
