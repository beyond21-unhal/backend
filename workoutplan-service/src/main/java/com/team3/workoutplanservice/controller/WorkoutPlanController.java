package com.team3.workoutplanservice.controller;


import com.team3.workoutplanservice.dto.request.WeeklyRequestDTO;
import com.team3.workoutplanservice.dto.request.WeeklyStatsDTO;
import com.team3.workoutplanservice.dto.request.WorkoutPlanRequest;
import com.team3.workoutplanservice.service.WorkoutPlanService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/workout")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    /** 운동 계획 생성 */
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> createWorkoutPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody WorkoutPlanRequest request
    ) {
        workoutPlanService.saveWorkoutPlan(userId, request);
        return ResponseEntity.ok("운동 계획 등록 완료");
    }

    /** 날짜별 운동 계획 조회 */
    @GetMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getWorkoutByDate(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(workoutPlanService.findByUserIdAndDate(userId, date));
    }

    /** 날짜별 운동 계획 수정 */
    @PatchMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateWorkoutPlanByDate(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestBody WorkoutPlanRequest request
    ) {
        workoutPlanService.updateByDate(userId, date, request);
        return ResponseEntity.ok("운동 계획 수정 완료");
    }

    /** 운동 완료 처리(isCompleted = true) */
    @PatchMapping("/complete")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> completeWorkoutPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        workoutPlanService.completeWorkout(userId, date);
        return ResponseEntity.ok("운동 완료 처리되었습니다.");
    }


    /** 날짜별 운동 계획 삭제 */
    @DeleteMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteWorkoutPlanByDate(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        workoutPlanService.deleteByDate(userId, date);
        return ResponseEntity.ok("운동 계획 삭제 완료");
    }

    @PostMapping("/weekly")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getWeeklyWorkoutPlanValue(@RequestBody WeeklyRequestDTO dto){
     return ResponseEntity.ok(workoutPlanService.getWeeklyValue(dto));
    }

    @PostMapping("/stats")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getWeeklyWorkoutPlanStats(@RequestBody WeeklyStatsDTO dto){
        return ResponseEntity.ok(workoutPlanService.getWeeklyRanking(dto));

    }
}
