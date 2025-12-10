package com.team3.reportservice.controller;

import com.team3.reportservice.dto.request.CreateReportDTO;
import com.team3.reportservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/last-week-report")
    @Operation(summary = "지난 주 리포트 생성 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> createLastWeekReport(
            @RequestBody CreateReportDTO dto,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        reportService.createLastWeekReport(userId, dto.plannedAmount(), dto.achievedAmount());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("지난 주 리포트가 생성되었습니다.");
    }

    @GetMapping("/all-report")
    @Operation(summary = "사용자별 전체 리포트 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> userViewReport(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getReportsByUserId(userId));
    }

    @GetMapping("/search-report/{date}")
    @Operation(summary = "날짜 검색을 통한 리포트 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> searchViewReport(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @PathVariable("date") LocalDate date
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getReportByDate(userId, date));
    }

    @DeleteMapping("/delete/{reportId}")
    @Operation(summary = "리포트 삭제 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteReport(
            @Parameter(hidden = true)  @RequestHeader("X-User-Id") Long userId,
            @PathVariable("reportId") Long reportId
    ) {
        reportService.deleteReportById(userId, reportId);
        return ResponseEntity.noContent().build();
    }
}
