package com.team3.statsservice.service;

import com.team3.statsservice.client.StatsClient;
import com.team3.statsservice.domian.Stats;
import com.team3.statsservice.dto.request.WeeklyStatsDTO;
import com.team3.statsservice.dto.response.CalorieRankingDto;
import com.team3.statsservice.dto.response.StatsViewDTO;
import com.team3.statsservice.dto.response.TimeRankingDto;
import com.team3.statsservice.dto.response.WeeklyStatsResponseDTO;
import com.team3.statsservice.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatsClient statsClient;

    @Transactional
    public void createLastWeekStats(Long userId) {
        LocalDate lastWeekStart = getLastWeekStart();
        LocalDate lastWeekEnd = getLastWeekEnd();

        WeeklyStatsDTO request = new WeeklyStatsDTO(userId, lastWeekStart, lastWeekEnd);
        WeeklyStatsResponseDTO summary = statsClient.getWeeklyStats(request);

        Stats stats = Stats.builder()
                .userId(userId)
                .startDate(lastWeekStart)
                .endDate(lastWeekEnd)
                .totalDuration(summary.totalDuration())
                .totalCalories(summary.totalCalories())
                .build();

        statsRepository.save(stats);
    }

    public List<StatsViewDTO> getStatsByUserId(Long userId) {
        return statsRepository.findByUserId(userId);
    }

    public List<TimeRankingDto> getLastWeekTimeRanking() {
        LocalDate lastWeekStart = getLastWeekStart();
        List<Stats> statsList = statsRepository.findByStartDateOrderByTotalDurationDesc(lastWeekStart);

        return buildTimeRanking(statsList);
    }

    public List<CalorieRankingDto> getLastWeekCalorieRanking() {
        LocalDate lastWeekStart = getLastWeekStart();
        List<Stats> statsList = statsRepository.findByStartDateOrderByTotalCaloriesDesc(lastWeekStart);

        return buildCalorieRanking(statsList);
    }

    public List<TimeRankingDto> getTimeRankingByDate(LocalDate date) {
        List<Stats> statsList = statsRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByTotalDurationDesc(date, date);

        return buildTimeRanking(statsList);
    }

    public List<CalorieRankingDto> getCalorieRankingByDate(LocalDate date) {
        List<Stats> statsList = statsRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByTotalCaloriesDesc(date, date);

        return buildCalorieRanking(statsList);
    }

    private LocalDate getLastWeekStart() { // 월요일 시작, 일요일 끝
        LocalDate today = LocalDate.now();
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);
        return thisWeekMonday.minusWeeks(1);
    }

    private LocalDate getLastWeekEnd() {
        return getLastWeekStart().plusDays(6);
    }

    private List<TimeRankingDto> buildTimeRanking(List<Stats> statsList) {
        List<TimeRankingDto> result = new ArrayList<>();

        int index = 0;
        int currentRank = 0;
        Integer prevValue = null;

        for (Stats stats : statsList) {
            index++;

            Integer value = stats.getTotalDuration();
            if (value == null) value = 0;

            if (prevValue == null || !prevValue.equals(value)) {
                currentRank = index;
                prevValue = value;
            }

            result.add(new TimeRankingDto(
                    currentRank,
                    stats.getUserId(),
                    value
            ));
        }

        return result;
    }

    private List<CalorieRankingDto> buildCalorieRanking(List<Stats> statsList) {
        List<CalorieRankingDto> result = new ArrayList<>();

        int index = 0;
        int currentRank = 0;
        Integer prevValue = null;

        for (Stats stats : statsList) {
            index++;

            Integer value = stats.getTotalCalories();
            if (value == null) value = 0;

            if (prevValue == null || !prevValue.equals(value)) {
                currentRank = index;
                prevValue = value;
            }

            result.add(new CalorieRankingDto(
                    currentRank,
                    stats.getUserId(),
                    value
            ));
        }

        return result;
    }
}
