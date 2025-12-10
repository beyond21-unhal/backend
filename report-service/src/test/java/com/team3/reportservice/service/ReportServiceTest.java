package com.team3.reportservice.service;

import com.team3.reportservice.domain.Report;
import com.team3.reportservice.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
    }

    @DisplayName("지난 주 리포트 생성")
    @Test
    void createLastWeekReport_success_void() {
        // given
        Long userId = 1L;
        Integer plannedAmount = 1000;
        Integer achievedAmount = 800;

        // when
        reportService.createLastWeekReport(userId, plannedAmount, achievedAmount);

        // then
        Report report = reportRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new AssertionError("리포트가 저장되지 않았습니다."));

        assertEquals(userId, report.getUserId());
        assertEquals(plannedAmount, report.getPlannedAmount());
        assertEquals(achievedAmount, report.getAchievedAmount());
        assertEquals(plannedAmount - achievedAmount, report.getResultValue());
        assertNotNull(report.getStartDate());
        assertNotNull(report.getEndDate());
    }
}