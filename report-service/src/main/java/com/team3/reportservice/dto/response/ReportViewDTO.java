package com.team3.reportservice.dto.response;

import com.team3.reportservice.domain.Report;

import java.time.LocalDate;

public record ReportViewDTO (
        LocalDate startDate,
        LocalDate endDate,
        Integer plannedAmount,
        Integer achievedAmount,
        String resultValue
) {

    public static ReportViewDTO fromEntity(Report report) {

        int result = report.getResultValue();

        String message;
        if (result > 0) {
            message = "계획보다 살짝 부족했어요. " + result + "칼로리만 더하면 목표 달성! 다음 주엔 분발해볼까요?";
        } else if (result < 0) {
            message = "멋져요! 목표를 " + Math.abs(result) + "칼로리 초과 달성했어요! 다음 주도 기대할게요.";
        } else {
            message = "완벽해요! 계획한 칼로리를 정확히 달성했어요!";
        }

        return new ReportViewDTO (
                report.getStartDate(),
                report.getEndDate(),
                report.getPlannedAmount(),
                report.getAchievedAmount(),
                message
        );
    }
}
