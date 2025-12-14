package com.team3.reportservice.service;

import com.team3.reportservice.client.ReportClient;
import com.team3.reportservice.domain.Report;
import com.team3.reportservice.dto.request.WeeklyRequestDTO;
import com.team3.reportservice.dto.response.ReportViewDTO;
import com.team3.reportservice.dto.response.WeeklySummaryDTO;
import com.team3.reportservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportClient reportClient;

    @Transactional
    public void createLastWeekReport(Long userId) {
        LocalDate lastWeekStart = getLastWeekStart();
        LocalDate lastWeekEnd = getLastWeekEnd();

        // resultValue 계산 (계획한 칼로리량 - 달성량)
        WeeklyRequestDTO request = new WeeklyRequestDTO(userId, lastWeekStart, lastWeekEnd);
        WeeklySummaryDTO summary = reportClient.getWeeklySummary(request);

        int plannedAmount = summary.plannedAmount() == null ? 0 : summary.plannedAmount();
        int achievedAmount = summary.achievedAmount() == null ? 0 : summary.achievedAmount();
        int resultValue = plannedAmount - achievedAmount;

        Report report = Report.builder()
                .userId(userId)
                .startDate(lastWeekStart)
                .endDate(lastWeekEnd)
                .plannedAmount(plannedAmount)
                .achievedAmount(achievedAmount)
                .resultValue(resultValue)
                .build();

        reportRepository.save(report);
    }

    public List<ReportViewDTO> getReportsByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUserId(userId);
        return reports.stream()
                .map(ReportViewDTO::fromEntity)
                .toList();
    }

    public ReportViewDTO getReportByDate(Long userId, LocalDate date) {
        Report report = reportRepository
                .findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(userId, date, date);
        return ReportViewDTO.fromEntity(report);
    }

    @Transactional
    public void deleteReportById(Long userId, Long reportId) {

        // exception : 삭제할 리포트가 존재하는지 체크
        if (!reportRepository.existsByUserIdAndReportId(userId, reportId)) {
            throw new IllegalStateException("리포트가 존재하지 않습니다.");
        }

        reportRepository.deleteByUserIdAndReportId(userId, reportId);
    }

    private LocalDate getLastWeekStart() { // 월요일 시작, 일요일 끝
        LocalDate today = LocalDate.now();
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);
        return thisWeekMonday.minusWeeks(1);
    }

    private LocalDate getLastWeekEnd() {
        return getLastWeekStart().plusDays(6);
    }
}
