package com.team3.workoutplanservice.dto.response;

import java.time.LocalDate;

public record WorkoutPlanResponse (
    Long workoutPlanId,
    String dayOfWeek,
    String exerciseType,
    String category,
    String difficulty,
    String bodyPart,
    LocalDate date,
    int workoutRecord, // 운동시간
    boolean isCompleted,  // 달성여부
    int burnedCalories //소비 칼로리

) {}
