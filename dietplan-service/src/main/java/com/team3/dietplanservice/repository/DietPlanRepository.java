package com.team3.dietplanservice.repository;


import com.team3.dietplanservice.domain.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {


    List<DietPlan> findByUserIdAndDate(Long userId, LocalDate date);
}


