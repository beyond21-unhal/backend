package com.team3.reportservice.client;

import com.team3.reportservice.dto.request.WeeklyRequestDTO;
import com.team3.reportservice.dto.response.WeeklySummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "workoutplan-service",
        path = "/workout"
)
public interface ReportClient {

    @PostMapping("/weekly")
    WeeklySummaryDTO getWeeklySummary(@RequestBody WeeklyRequestDTO weeklyRequestDTO);
}
