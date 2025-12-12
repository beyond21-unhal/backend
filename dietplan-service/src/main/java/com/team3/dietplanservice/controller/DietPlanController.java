package com.team3.dietplanservice.controller;

import com.team3.dietplanservice.dto.request.DietPlanRequest;
import com.team3.dietplanservice.service.DietPlanService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@RestController
@RequestMapping("/dietplan")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    /**
     * 식단 등록 (이미지 포함)
     */
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> createDietPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,

            @RequestPart("diet") DietPlanRequest dietPlan,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        dietPlanService.saveDietPlan(userId, dietPlan, image);
        return ResponseEntity.ok("식단 등록을 완료했습니다.");
    }

    /**
     * 날짜별 식단 조회
     */
    @GetMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findTodayDietPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(dietPlanService.findTodayDietPlan(userId, date));
    }

    /**
     * 식단 수정 (이미지 포함)
     */
    @PatchMapping("/{dietPlanId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateDietPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,

            @PathVariable Long dietPlanId,
            @RequestPart("diet") DietPlanRequest dietPlan,
            @RequestPart(value = "image", required = false) MultipartFile newImage
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        dietPlanService.updateDietPlan(dietPlanId, dietPlan, newImage);
        return ResponseEntity.ok("식단 수정을 완료했습니다.");
    }

    /**
     * 식단 삭제
     */
    @DeleteMapping("/{dietPlanId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteDietPlan(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,

            @PathVariable Long dietPlanId
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        dietPlanService.deleteDietPlan(dietPlanId);
        return ResponseEntity.ok("식단 삭제를 완료했습니다.");
    }
}
