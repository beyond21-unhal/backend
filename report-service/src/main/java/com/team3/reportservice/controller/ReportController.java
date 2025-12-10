package com.team3.reportservice.controller;

import com.team3.reportservice.dto.request.CreateReportDTO;
import com.team3.reportservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/last-week-report")
    @Operation(summary = "지난 주 리포트 생성 API입니다.")
    public ResponseEntity<String> createLastWeekReport(@RequestBody CreateReportDTO dto) {
        reportService.createLastWeekReport(dto.userId(), dto.plannedAmount(), dto.achievedAmount());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("지난 주 리포트가 생성되었습니다.");
    }
}
