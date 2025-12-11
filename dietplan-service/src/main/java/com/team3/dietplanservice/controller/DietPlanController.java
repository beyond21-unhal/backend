package com.team3.dietplanservice.controller;

import com.team3.dietplanservice.dto.request.DietPlanRequest;
import com.team3.dietplanservice.service.DietPlanService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/dietplan")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;


    @PostMapping("/dietplan1")
    @SecurityRequirement (name = "JWT")
    public ResponseEntity<?> createDietPlan(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") String role,

            @RequestBody DietPlanRequest dietPlan) {
        dietPlanService.saveDietPlan(userId, dietPlan);
        return ResponseEntity.ok("식단 등록을 완료했습니다");
    }


    @GetMapping("/dietplan2")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findTodayDietPlan(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") String role,

            @RequestParam
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
            )
            LocalDate date
    )
    {
        return ResponseEntity.ok(
                dietPlanService.findTodayDietPlan(userId, date)
        );
    }

    @PatchMapping("/dietplan3/{dietPlanId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateDietPlan(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") String role,

            @RequestParam Long dietPlanId,
            @RequestBody DietPlanRequest dietPlanRequest
    ) {
        dietPlanService.updateDietPlan(dietPlanId, dietPlanRequest);
        return ResponseEntity.ok("식단 수정을 완료했습니다.");
    }


    @DeleteMapping("/dietplan3/{dietPlanId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteDietPlan(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") String role,

            @RequestParam Long dietPlanId
    ) {
        dietPlanService.deleteDietPlan(dietPlanId);
        return ResponseEntity.ok("식단 삭제를 완료했습니다");
    }
}
