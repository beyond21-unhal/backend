package com.team3.statsservice.repository;

import com.team3.statsservice.domian.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    List<Stats> findByUserId(Long userId);

    List<Stats> findByStartDateOrderByTotalDurationDesc(LocalDate startDate);

    List<Stats> findByStartDateOrderByTotalCaloriesDesc(LocalDate startDate);

    List<Stats> findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByTotalDurationDesc(LocalDate startDate, LocalDate endDate);

    List<Stats> findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByTotalCaloriesDesc(LocalDate startDate, LocalDate endDate);

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
}
