package com.team3.reportservice.dto.response;

import com.team3.reportservice.domain.Report;

import java.time.LocalDate;

public record ReportViewDTO (
        LocalDate startDate,
        LocalDate endDate,
        Integer plannedAmount,
        Integer achievedAmount,
        Integer resultValue
) {

    public static ReportViewDTO fromEntity(Report report) {
        return new ReportViewDTO (
                report.getStartDate(),
                report.getEndDate(),
                report.getPlannedAmount(),
                report.getAchievedAmount(),
                report.getResultValue()
        );
    }
}
