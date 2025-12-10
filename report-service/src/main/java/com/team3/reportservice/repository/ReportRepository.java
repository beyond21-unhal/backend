package com.team3.reportservice.repository;

import com.team3.reportservice.domain.Report;
import com.team3.reportservice.dto.response.ReportViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<ReportViewDTO> findByUserId(Long userId);

    ReportViewDTO findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long userId, LocalDate startDate, LocalDate endDate);

    void deleteByUserIdAndReportId(Long userId, Long reportId);

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);

    // 테스트용
    Optional<Report> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
