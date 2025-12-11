package com.team3.statsservice.client;

import com.team3.statsservice.dto.request.WeeklyStatsDTO;
import com.team3.statsservice.dto.response.WeeklyStatsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "workoutplan-service",
        path = "/workout"
)
public interface StatsClient {

    @PostMapping("/stats")
    WeeklyStatsResponseDTO getWeeklyStats(@RequestBody WeeklyStatsDTO weeklyStatsDTO);
}
