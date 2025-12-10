package com.team3.statsservice.service;

import com.team3.statsservice.domian.Stats;
import com.team3.statsservice.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @jakarta.transaction.Transactional
    public void createLastWeekStats(Long userId, Integer totalDuration, Integer totalCalories) {
        LocalDate lastWeekStart = getLastWeekStart();
        LocalDate lastWeekEnd = getLastWeekEnd();

        // 이미 지난 주 랭킹이 있는지 확인
        if (statsRepository.existsByUserIdAndStartDate(userId, lastWeekStart)) {
            throw new IllegalStateException("이미 지난 주 통계가 존재합니다.");
        }

        Stats stats = Stats.builder()
                .userId(userId)
                .startDate(lastWeekStart)
                .endDate(lastWeekEnd)
                .totalDuration(totalDuration)
                .totalCalories(totalCalories)
                .build();

        statsRepository.save(stats);
    }

    private LocalDate getLastWeekStart() { // 월요일 시작, 일요일 끝
        LocalDate today = LocalDate.now();
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);
        return thisWeekMonday.minusWeeks(1);
    }

    private LocalDate getLastWeekEnd() {
        return getLastWeekStart().plusDays(6);
    }
}
