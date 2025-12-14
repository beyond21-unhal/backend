package com.team3.workoutplanservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "workout_plan")
public class WorkoutPlan {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutplanId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private String exerciseType;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String bodyPart; //userId를 제외한 값은 enum으로 받고, service 후에 enum class로 수정하기.

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int workoutRecord; // 운동시간

    @Column(nullable = false)
    private boolean isCompleted=false; // 달성여부

    @Column(nullable = false)
    private int burnedCalories; //소비 칼로리



    @Builder
    public WorkoutPlan(Long userId, String dayOfWeek, String exerciseType, String category, String difficulty, String bodyPart, LocalDate date, int workoutRecord, boolean isCompleted, int burnedCalories) {
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.exerciseType = exerciseType;
        this.category = category;
        this.difficulty = difficulty;
        this.bodyPart = bodyPart;
        this.date = date;
        this.workoutRecord = workoutRecord;
        this.isCompleted = isCompleted;
        this.burnedCalories = burnedCalories;

    }

    public void update(
            String dayOfWeek,
            String exerciseType,
            String category,
            String difficulty,
            String bodyPart,
            LocalDate date,
            int workoutRecord,
            int burnedCalories,
            boolean isCompleted
    ) {
        this.dayOfWeek = dayOfWeek;
        this.exerciseType = exerciseType;
        this.category = category;
        this.difficulty = difficulty;
        this.bodyPart = bodyPart;
        this.date = date;
        this.workoutRecord = workoutRecord;
        this.burnedCalories = burnedCalories;
        this.isCompleted = isCompleted;

    }


    public void assignworkoutId(Long workoutId) {

        this.workoutplanId = workoutId;
    }

    public void complete() {
        this.isCompleted = true;
    }
}
