package com.team3.dietplanservice.domain;

import com.team3.dietplanservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diet_plan")
public class DietPlan extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long dietPlanId;

        @Column(nullable = false)
        private Long userId;

        @Column(nullable = false)
        private String mealTime;

        @Column(nullable = false)
        private Long foodId;

        @Column(nullable = false)
        private String foodName;

        @Column(nullable = false)
        private Integer calories;

        @Column(nullable = false)
        private LocalDate date;

        private String imageUrl;

    @Builder
    public DietPlan(Long userId, String mealTime, Long foodId,
                    String foodName, Integer calories, LocalDate date,
                    String imageUrl) {
        this.userId = userId;
        this.mealTime = mealTime;
        this.foodId = foodId;
        this.foodName = foodName;
        this.calories = calories;
        this.date = date;
        this.imageUrl = imageUrl;
    }

        public void attachImage(String imageUrl) {
            this.imageUrl = imageUrl;
    }

        public void update(String mealTime,
                           Long foodId,
                           String foodName,
                           Integer calories,
                           LocalDate date
                           ) {
            this.mealTime = mealTime;
            this.foodId = foodId;
            this.foodName = foodName;
            this.calories = calories;
            this.date = date;
        }

        public void assigndietId(Long dietId) {
            this.dietPlanId = dietId;
        }
    }


