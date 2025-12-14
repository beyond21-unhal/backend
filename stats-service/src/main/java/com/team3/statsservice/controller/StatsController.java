package com.team3.statsservice.controller;

import com.team3.statsservice.dto.response.ApiResponse;
import com.team3.statsservice.dto.response.CalorieRankingDto;
import com.team3.statsservice.dto.response.StatsViewDTO;
import com.team3.statsservice.dto.response.TimeRankingDto;
import com.team3.statsservice.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/last-week-stats")
    @Operation(summary = "지난 주 통계 생성 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<String>> createLastWeekStats(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        try {
            statsService.createLastWeekStats(userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("지난 주 랭킹을 생성했습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(
                    ApiResponse.success("이미 지난 주 통계가 존재합니다.")
            );
        }
    }

    @GetMapping("/user-stats")
    @Operation(summary = "사용자별 통계 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<StatsViewDTO>>> viewStatsByUserId(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        List<StatsViewDTO> stats = statsService.getStatsByUserId(userId);

        if (stats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(statsService.getStatsByUserId(userId)));
    }

    @GetMapping("/last-week-time-ranking")
    @Operation(summary = "지난 주 운동량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<TimeRankingDto>>> viewLastWeekTimeRanking() {
        List<TimeRankingDto> rankingList = statsService.getLastWeekTimeRanking();

        if (rankingList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(statsService.getLastWeekTimeRanking()));
    }

    @GetMapping("/last-week-calorie-ranking")
    @Operation(summary = "지난 주 칼로리 소모량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<CalorieRankingDto>>> viewLastWeekCalorieRanking() {

        List<CalorieRankingDto> rankingList = statsService.getLastWeekCalorieRanking();

        if (rankingList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(statsService.getLastWeekCalorieRanking()));
    }

    @GetMapping("/search-time-ranking/{date}")
    @Operation(summary = "날짜 검색을 통한 운동량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<TimeRankingDto>>> viewTimeRankingByDate(
            @PathVariable("date") LocalDate date
    ) {
        List<TimeRankingDto> rankingList = statsService.getTimeRankingByDate(date);

        if (rankingList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(statsService.getTimeRankingByDate(date)));
    }

    @GetMapping("/search-calorie-ranking/{date}")
    @Operation(summary = "날짜 검색을 통한 칼로리 소모량 랭킹 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<CalorieRankingDto>>> viewCalorieRankingByDate(
            @PathVariable("date") LocalDate date
    ) {
        List<CalorieRankingDto> rankingList = statsService.getCalorieRankingByDate(date);

        if (rankingList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(statsService.getCalorieRankingByDate(date)));
    }
}
