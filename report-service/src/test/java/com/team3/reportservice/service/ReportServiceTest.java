package com.team3.reportservice.service;

import com.team3.reportservice.client.ReportClient;
import com.team3.reportservice.domain.Report;
import com.team3.reportservice.dto.request.WeeklyRequestDTO;
import com.team3.reportservice.dto.response.ReportViewDTO;
import com.team3.reportservice.dto.response.WeeklySummaryDTO;
import com.team3.reportservice.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @MockitoBean
    private ReportClient reportClient;

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

        WeeklySummaryDTO summary = new WeeklySummaryDTO(
                userId,
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                plannedAmount,
                achievedAmount
        );

        when(reportClient.getWeeklySummary(any(WeeklyRequestDTO.class)))
                .thenReturn(summary);

        // when
        reportService.createLastWeekReport(userId);

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

    @DisplayName("사용자별 전체 리포트 조회")
    @Test
    void getReportsByUserId() {
        // given
        Long userId = 1L;
        Integer plannedAmount = 1000;
        Integer achievedAmount = 800;

        WeeklySummaryDTO summary = new WeeklySummaryDTO(
                userId,
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                plannedAmount,
                achievedAmount
        );

        when(reportClient.getWeeklySummary(any(WeeklyRequestDTO.class)))
                .thenReturn(summary);

        reportService.createLastWeekReport(userId);

        // when
        List<ReportViewDTO> reports = reportService.getReportsByUserId(userId);

        // then
        assertFalse(reports.isEmpty(), "해당 아이디의 리포트가 존재하지 않습니다.");

        ReportViewDTO first = reports.get(0);

        assertNotNull(first.startDate());
        assertNotNull(first.endDate());
        assertEquals(plannedAmount, first.plannedAmount());
        assertEquals(achievedAmount, first.achievedAmount());

        String message = first.resultValue();
        assertNotNull(message);
        assertTrue(message.contains(String.valueOf(plannedAmount - achievedAmount)));
    }

    @DisplayName("날짜 검색을 통한 리포트 조회")
    @Test
    void getReportByDate() {
        // given
        Long userId = 1L;

        LocalDate lastWeekStart = LocalDate.now()
                .minusWeeks(1)
                .with(DayOfWeek.MONDAY);
        LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

        Report report = Report.builder()
                .userId(userId)
                .startDate(lastWeekStart)
                .endDate(lastWeekEnd)
                .plannedAmount(1000)
                .achievedAmount(800)
                .resultValue(200)
                .build();

        reportRepository.save(report);

        LocalDate searchDate = lastWeekStart.plusDays(3);

        // when
        ReportViewDTO foundReport = reportService.getReportByDate(userId, searchDate);

        // then
        assertNotNull(foundReport);
        assertEquals(lastWeekStart, foundReport.startDate());
        assertEquals(lastWeekEnd, foundReport.endDate());
        assertEquals(1000, foundReport.plannedAmount());
        assertEquals(800, foundReport.achievedAmount());

        String message = foundReport.resultValue();
        assertNotNull(message);
        assertTrue(message.contains("200"));
    }

    @DisplayName("리포트 삭제")
    @Test
    void deleteReportById() {
        // given
        Long userId = 1L;

        Report report = Report.builder()
                .userId(userId)
                .startDate(LocalDate.now().with(DayOfWeek.MONDAY))
                .endDate(LocalDate.now().with(DayOfWeek.SUNDAY))
                .plannedAmount(1000)
                .achievedAmount(800)
                .resultValue(200)
                .build();

        Report saved = reportRepository.save(report);
        Long reportId = saved.getReportId();

        // when
        reportService.deleteReportById(userId, reportId);

        // then
        assertFalse(reportRepository.findById(reportId).isPresent(), "리포트가 삭제되지 않았습니다.");
    }
}