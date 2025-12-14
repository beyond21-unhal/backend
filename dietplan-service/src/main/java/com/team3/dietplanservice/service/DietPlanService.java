package com.team3.dietplanservice.service;

import com.team3.dietplanservice.domain.DietPlan;
import com.team3.dietplanservice.dto.request.DietPlanRequest;
import com.team3.dietplanservice.dto.response.DietPlanResponse;
import com.team3.dietplanservice.repository.DietPlanRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final MinioFileService minioFileService;

    /**
     * 식단 등록 (이미지 포함)
     */
    @Transactional
    public DietPlan saveDietPlan(Long userId, DietPlanRequest dietPlan, MultipartFile image)
            throws Exception {

        DietPlan dietPlanEntity = dietPlan.toEntity(userId);

        if (image != null && !image.isEmpty()) {
            String imageUrl = minioFileService.saveImageFile(image);
            dietPlanEntity.attachImage(imageUrl);
        }

        return dietPlanRepository.save(dietPlanEntity);
    }
    /**
     * 날짜별 식단 조회
     */
    public List<DietPlanResponse> findTodayDietPlan(Long userId, LocalDate date) {
        List<DietPlan> temp = dietPlanRepository.findByUserIdAndDate(userId, date);

        List<DietPlanResponse> result = new ArrayList<>();
        for (DietPlan dietPlan : temp) {
            result.add(
                    DietPlanResponse.builder()
                            .dietPlanId(dietPlan.getDietPlanId())
                            .mealTime(dietPlan.getMealTime())
                            .calories(dietPlan.getCalories())
                            .foodId(dietPlan.getFoodId())
                            .foodName(dietPlan.getFoodName())
                            .imageUrl(dietPlan.getImageUrl())
                            .build()
            );

        }

        return result;
    }

    /**
     * 식단 수정 (이미지 포함)
     */
    @Transactional
    public void updateDietPlan(Long dietPlanId, DietPlanRequest dietPlan, MultipartFile newImage)
            throws Exception {

        DietPlan origin = dietPlanRepository.findById(dietPlanId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식단이 존재하지 않습니다."));

        // 이미지 변경 요청이 있는 경우
        if (newImage != null && !newImage.isEmpty()) {

            // 기존 이미지 삭제
            if (origin.getImageUrl() != null) {
                minioFileService.deleteImageByUrl(origin.getImageUrl());
            }

            // 새 이미지 업로드
            String newImageUrl = minioFileService.saveImageFile(newImage);
            origin.attachImage(newImageUrl);
        }

        // 텍스트 부분 수정
        origin.update(
                dietPlan.mealTime(),
                dietPlan.foodId(),
                dietPlan.foodName(),
                dietPlan.calories(),
                dietPlan.date()
        );
    }

    /**
     * 식단 삭제
     */
    @Transactional
    public void deleteDietPlan(Long dietPlanId)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        DietPlan dietPlan = dietPlanRepository.findById(dietPlanId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식단이 존재하지 않습니다."));

        // 이미지 삭제
        if (dietPlan.getImageUrl() != null) {
            minioFileService.deleteImageByUrl(dietPlan.getImageUrl());
        }

        dietPlanRepository.delete(dietPlan);
    }
}
