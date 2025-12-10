package com.team3.statsservice.repository;

import com.team3.statsservice.domian.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    List<Stats> findByUserId(Long userId);

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
}
