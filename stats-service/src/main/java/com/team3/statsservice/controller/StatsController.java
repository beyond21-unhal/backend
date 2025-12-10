package com.team3.statsservice.controller;

import com.team3.statsservice.dto.request.CreateStatsDTO;
import com.team3.statsservice.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/last-week-stats")
    @Operation(summary = "지난 주 통계 생성 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> createLastWeekStats(
            @RequestBody CreateStatsDTO dto,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        statsService.createLastWeekStats(userId, dto.totalDuration(), dto.totalCalories());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("지난 주 랭킹이 생성되었습니다.");
    }

    @GetMapping("/user-stats/{userId}")
    @Operation(summary = "사용자별 통계 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> viewStatsByUserId(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statsService.getStatsByUserId(userId));
    }

    @GetMapping("/last-week-time-ranking")
    @Operation(summary = "지난 주 운동량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> viewLastWeekTimeRanking() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statsService.getLastWeekTimeRanking());
    }

    @GetMapping("/last-week-calorie-ranking")
    @Operation(summary = "지난 주 칼로리 소모량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> viewLastWeekCalorieRanking() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statsService.getLastWeekCalorieRanking());
    }
}
