package com.team3.statsservice.service;

import com.team3.statsservice.domian.Stats;
import com.team3.statsservice.repository.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private StatsRepository statsRepository;

    @BeforeEach
    void setUp() {
        statsRepository.deleteAll();
    }

    @DisplayName("지난 주 통계 생성")
    @Test
    void createLastWeekStats() {
        // given
        Long userId = 1L;
        Integer totalDuration = 60;
        Integer totalCalories = 500;

        // when
        statsService.createLastWeekStats(userId, totalDuration, totalCalories);

        // then
        Stats stats = statsRepository.findTopByUserIdOrderByStartDateDesc(userId)
                .orElseThrow(() -> new AssertionError("통계가 저장되지 않았습니다."));

        assertEquals(userId, stats.getUserId());
        assertEquals(totalDuration, stats.getTotalDuration());
        assertEquals(totalCalories, stats.getTotalCalories());
        assertNotNull(stats.getStartDate());
        assertNotNull(stats.getEndDate());
    }
}