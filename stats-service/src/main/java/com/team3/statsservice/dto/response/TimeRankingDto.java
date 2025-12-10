package com.team3.statsservice.dto.response;

public record TimeRankingDto (

        Long userId,
        int timeRank,
        Integer totalDuration
) {}
