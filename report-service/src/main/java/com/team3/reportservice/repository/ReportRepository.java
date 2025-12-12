package com.team3.reportservice.repository;

import com.team3.reportservice.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserId(Long userId);
    Report findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long userId, LocalDate startDate, LocalDate endDate);
    void deleteByUserIdAndReportId(Long userId, Long reportId);

    // 예외 처리용
    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
    boolean existsByUserIdAndReportId(Long userId, Long reportId);

    // 테스트용
    Optional<Report> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
