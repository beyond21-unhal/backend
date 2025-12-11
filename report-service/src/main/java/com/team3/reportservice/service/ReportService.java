package com.team3.reportservice.service;

import com.team3.reportservice.domain.Report;
import com.team3.reportservice.dto.response.ReportViewDTO;
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

    @Transactional
    public void createLastWeekReport(Long userId, Integer plannedAmount, Integer achievedAmount) {
        LocalDate lastWeekStart = getLastWeekStart();
        LocalDate lastWeekEnd = getLastWeekEnd();

        // exception : 이미 지난 주 리포트가 있는지 체크
        if (reportRepository.existsByUserIdAndStartDate(userId, lastWeekStart)) {
            throw new IllegalStateException("이미 지난 주 리포트가 존재합니다.");
        }

        // resultValue 계산 (계획한 칼로리량 - 달성량)
        plannedAmount = (plannedAmount == null) ? 0 : plannedAmount;
        achievedAmount = (achievedAmount == null) ? 0 : achievedAmount;
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
