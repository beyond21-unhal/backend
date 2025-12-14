package com.team3.dietplanservice.service;

import com.team3.dietplanservice.domain.DietPlan;
import com.team3.dietplanservice.dto.request.DietPlanRequest;
import com.team3.dietplanservice.repository.DietPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietPlanServiceTests {

    @Mock
    private DietPlanRepository dietPlanRepository;

    @Mock
    private MinioFileService minioFileService;

    @InjectMocks
    private DietPlanService dietPlanService;

    @Test
    void saveDietPlan_withImage_success() throws Exception {

        DietPlanRequest request = new DietPlanRequest(
                "사과",       // foodName
                100,         // calories
                1L,          // foodId
                "아침",        // mealTime
                LocalDate.now()
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "dummy".getBytes()
        );

        DietPlan savedEntity = request.toEntity(1L);

        when(minioFileService.saveImageFile(any())).thenReturn("http://minio/test.jpg");
        when(dietPlanRepository.save(any())).thenReturn(savedEntity);

        DietPlan result = dietPlanService.saveDietPlan(1L, request, image);

        assertThat(result).isNotNull();
        verify(minioFileService, times(1)).saveImageFile(any());
        verify(dietPlanRepository, times(1)).save(any());
    }


    @Test
    void saveDietPlan_withoutImage_success() throws Exception {

        DietPlanRequest request = new DietPlanRequest(
                "바나나",
                200,
                3L,
                "점심",
                LocalDate.now()
        );

        DietPlan dietPlan = request.toEntity(1L);

        when(dietPlanRepository.save(any())).thenReturn(dietPlan);

        DietPlan result = dietPlanService.saveDietPlan(1L, request, null);

        assertThat(result).isNotNull();
        verify(minioFileService, never()).saveImageFile(any());
        verify(dietPlanRepository, times(1)).save(any());
    }


    @Test
    void findDietPlan_success() {

        LocalDate date = LocalDate.of(2025, 1, 1);

        when(dietPlanRepository.findByUserIdAndDate(1L, date))
                .thenReturn(List.of(mock(DietPlan.class)));

        List<DietPlan> result = dietPlanService.findTodayDietPlan(1L, date);

        assertThat(result).hasSize(1);
        verify(dietPlanRepository, times(1))
                .findByUserIdAndDate(1L, date);
    }


    @Test
    void updateDietPlan_withNewImage_success() throws Exception {

        DietPlan origin = mock(DietPlan.class);

        DietPlanRequest request = new DietPlanRequest(
                "닭가슴살",
                300,
                5L,
                "저녁",
                LocalDate.now()
        );

        MockMultipartFile newImage = new MockMultipartFile(
                "image",
                "test2.jpg",
                "image/jpeg",
                "dummy2".getBytes()
        );

        when(dietPlanRepository.findById(10L)).thenReturn(Optional.of(origin));
        when(origin.getImageUrl()).thenReturn("oldImage.jpg");
        when(minioFileService.saveImageFile(any())).thenReturn("newImage.jpg");

        dietPlanService.updateDietPlan(10L, request, newImage);

        verify(minioFileService, times(1)).deleteImageByUrl("oldImage.jpg");
        verify(minioFileService, times(1)).saveImageFile(any());
        verify(origin, times(1)).attachImage("newImage.jpg");

        verify(origin, times(1)).update(
                request.mealTime(),
                request.foodId(),
                request.foodName(),
                request.calories(),
                request.date()
        );
    }


    @Test
    void updateDietPlan_notFound_fail() {

        DietPlanRequest request = new DietPlanRequest(
                "샐러드",
                150,
                2L,
                "아침",
                LocalDate.now()
        );

        when(dietPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> dietPlanService.updateDietPlan(99L, request, null));
    }


    @Test
    void deleteDietPlan_success() throws Exception {

        DietPlan dietPlan = mock(DietPlan.class);

        when(dietPlanRepository.findById(7L)).thenReturn(Optional.of(dietPlan));
        when(dietPlan.getImageUrl()).thenReturn("delete.jpg");

        dietPlanService.deleteDietPlan(7L);

        verify(minioFileService, times(1)).deleteImageByUrl("delete.jpg");
        verify(dietPlanRepository, times(1)).delete(dietPlan);
    }
}
