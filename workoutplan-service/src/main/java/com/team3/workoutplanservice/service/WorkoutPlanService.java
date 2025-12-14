package com.team3.workoutplanservice.service;

import com.team3.workoutplanservice.domain.WorkoutPlan;
import com.team3.workoutplanservice.dto.request.WeeklyRequestDTO;
import com.team3.workoutplanservice.dto.request.WeeklyStatsDTO;
import com.team3.workoutplanservice.dto.request.WorkoutPlanRequest;
import com.team3.workoutplanservice.dto.response.WeeklyStatsResponseDTO;
import com.team3.workoutplanservice.dto.response.WeeklySummaryDTO;
import com.team3.workoutplanservice.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Transactional
    public void saveWorkoutPlan(Long userId, WorkoutPlanRequest request) {
        WorkoutPlan plan = request.toEntity(userId);

        // 1. 오늘날짜 기준으로 데이터베이스에 값이 존재하는지 여부를 확인
        // 2-1. 오늘 날짜기준의 계획이 없으면 insert문을 실행
        // 2-2. 오늘 날짜기준의 계획이 존재하면 기존값을 upldate로 수행
        workoutPlanRepository.save(plan);
    }

    /** 날짜별 운동 계획 조회 */
    public WorkoutPlan findByUserIdAndDate(Long userId, LocalDate date) {
        return workoutPlanRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 운동 계획이 없습니다."));
    }

    /** 날짜 기반 수정 */
    @Transactional
    public void updateByDate(Long userId, LocalDate date, WorkoutPlanRequest request) {
        WorkoutPlan plan = findByUserIdAndDate(userId, date);

        plan.update(
                request.dayOfWeek(),
                request.exerciseType(),
                request.category(),
                request.difficulty(),
                request.bodyPart(),
                request.date(),
                request.workoutRecord(),
                request.burnedCalories(),
                request.isCompleted()
        );
    }

    /* 운동 완료 상태 수정 */
    @Transactional
    public void completeWorkout(Long userId, LocalDate date) {
        WorkoutPlan plan = findByUserIdAndDate(userId, date);

        plan.complete(); // isCompleted = true 로 변경
    }

    /** 날짜 기반 삭제 */
    @Transactional
    public void deleteByDate(Long userId, LocalDate date) {
        WorkoutPlan plan = findByUserIdAndDate(userId, date);
        workoutPlanRepository.delete(plan);
    }

    /** 기존 리스트 조회 (원한다면 유지) */
    public List<WorkoutPlan> findWorkoutPlans(Long userId) {
        return workoutPlanRepository.findByUserId(userId);
    }

    public WeeklySummaryDTO getWeeklyValue(WeeklyRequestDTO dto) {
        List<WorkoutPlan> allPlans = workoutPlanRepository.findAllByDateBetweenAndUserId(dto.startDate(), dto.endDate(), dto.userId());
        int plannedAmount = 0;
        int achievedAmount = 0;

        for (WorkoutPlan plan : allPlans) {
            plannedAmount+=plan.getBurnedCalories();
            if(plan.isCompleted()) {
                achievedAmount+=plan.getBurnedCalories();
            }

        }
        return WeeklySummaryDTO.builder()
                .userId(dto.userId())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .plannedAmount(plannedAmount)
                .achievedAmount(achievedAmount)
                .build();
    }

    public WeeklyStatsResponseDTO getWeeklyRanking(WeeklyStatsDTO dto) {

        List<WorkoutPlan> allPlans = workoutPlanRepository.findAllByDateBetweenAndUserId(dto.startDate(), dto.endDate(), dto.userId());
        int totalCalories = 0;
        int totalDuration = 0;

        for (WorkoutPlan plan : allPlans) {

            if(plan.isCompleted()) {
                totalCalories+=plan.getBurnedCalories();
                totalDuration+=plan.getWorkoutRecord();
            }

        }

        return WeeklyStatsResponseDTO.builder()
                .userId(dto.userId())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalCalories(totalCalories)
                .totalDuration(totalDuration)
                .build();

    }
}
