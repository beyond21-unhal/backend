package com.team3.reportservice.dto.request;

public record CreateReportDTO(
        Integer plannedAmount,
        Integer achievedAmount
) {}
