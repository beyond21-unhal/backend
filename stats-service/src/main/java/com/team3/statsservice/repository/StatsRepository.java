package com.team3.statsservice.repository;

import com.team3.statsservice.domian.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
}
