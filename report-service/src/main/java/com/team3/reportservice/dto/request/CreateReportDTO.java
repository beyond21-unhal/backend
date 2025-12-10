package com.team3.reportservice.dto.request;

public record CreateReportDTO(

        Long userId,
        Integer plannedAmount,
        Integer achievedAmount
) {}
