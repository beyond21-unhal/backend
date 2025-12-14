package com.team3.reportservice.controller;

import com.team3.reportservice.dto.request.CreateReportDTO;
import com.team3.reportservice.dto.response.ApiResponse;
import com.team3.reportservice.dto.response.ReportViewDTO;
import com.team3.reportservice.service.ReportService;
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
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/last-week-report")
    @Operation(summary = "지난 주 리포트 생성 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<String>> createLastWeekReport(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        try {
            reportService.createLastWeekReport(userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("지난 주 리포트를 생성했습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(
                    ApiResponse.success("이미 지난 주 리포트가 존재합니다.")
            );
        }
    }

    @GetMapping("/all-report")
    @Operation(summary = "사용자별 전체 리포트 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<ReportViewDTO>>> userViewReport(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ) {
        List<ReportViewDTO> reports = reportService.getReportsByUserId(userId);

        if (reports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reportService.getReportsByUserId(userId)));
    }

    @GetMapping("/search-report/{date}")
    @Operation(summary = "날짜 검색을 통한 리포트 조회 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<ReportViewDTO>> searchViewReport(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @PathVariable("date") LocalDate date
    ) {
        ReportViewDTO report = reportService.getReportByDate(userId, date);

        if (report == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(reportService.getReportByDate(userId, date)));
    }

    @DeleteMapping("/delete/{reportId}")
    @Operation(summary = "리포트 삭제 API입니다.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<String>> deleteReport(
            @Parameter(hidden = true)  @RequestHeader("X-User-Id") Long userId,
            @PathVariable("reportId") Long reportId
    ) {
        reportService.deleteReportById(userId, reportId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("리포트가 삭제되었습니다."));
    }
}
