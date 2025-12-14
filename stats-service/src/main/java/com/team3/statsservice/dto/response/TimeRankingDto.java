package com.team3.statsservice.dto.response;

public record TimeRankingDto (

        int timeRank,
        Long userId,
        Integer totalDuration
) {}
