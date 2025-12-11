package com.team3.reportservice.client;

import com.team3.reportservice.dto.response.WeeklySummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(
        name = "WORKOUTPLAN-SERVICE",
        path = "/workoutplan"
)
public interface ReportClient {

    @GetMapping("/weekly-summary")
    WeeklySummaryDto getWeeklySummary(
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    );
}
