package com.team3.statsservice.repository;

import com.team3.statsservice.domian.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats, Long> {

}
