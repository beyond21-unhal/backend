package com.team3.workoutplanservice.repository;

import com.team3.workoutplanservice.domain.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    List<WorkoutPlan> findByUserId(Long userId);

    Optional<WorkoutPlan> findByUserIdAndDate(Long userId, LocalDate date);

    List<WorkoutPlan> findAllByDateBetweenAndUserId(LocalDate start, LocalDate end, Long userId);
}