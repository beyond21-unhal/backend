package com.team3.statsservice.service;

import com.team3.statsservice.domian.Stats;
import com.team3.statsservice.dto.response.TimeRankingDto;
import com.team3.statsservice.repository.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @DisplayName("사용자별 전체 통계 조회")
    @Test
    void getStatsByUserId() {
        // given
        Long userId = 1L;
        statsService.createLastWeekStats(userId, 30, 300);

        // when
        List<Stats> stats = statsService.getStatsByUserId(userId);

        // then
        assertFalse(stats.isEmpty(), "해당 아이디의 통계가 존재하지 않습니다.");
        assertEquals(userId, stats.get(0).getUserId());
    }

    @DisplayName("지난 주 운동량 랭킹 조회 테스트")
    @Test
    void getLastWeekTimeRanking() {
        // given
        Long user1 = 1L;
        Long user2 = 2L;
        Long user3 = 3L;

        statsService.createLastWeekStats(user1, 30, 300);
        statsService.createLastWeekStats(user2, 60, 500);
        statsService.createLastWeekStats(user3, 45, 400);

        // when
        List<TimeRankingDto> ranking = statsService.getLastWeekTimeRanking();

        // then
        assertEquals(3, ranking.size());

        assertEquals(user2, ranking.get(0).userId());
        assertEquals(1, ranking.get(0).timeRank());
        assertEquals(60, ranking.get(0).totalDuration());

        assertEquals(user3, ranking.get(1).userId());
        assertEquals(2, ranking.get(1).timeRank());
        assertEquals(45, ranking.get(1).totalDuration());

        assertEquals(user1, ranking.get(2).userId());
        assertEquals(3, ranking.get(2).timeRank());
        assertEquals(30, ranking.get(2).totalDuration());
    }
}