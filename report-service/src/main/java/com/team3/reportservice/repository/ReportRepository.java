package com.team3.reportservice.repository;

import com.team3.reportservice.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserId(Long userId);

    Report findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long userId, LocalDate startDate, LocalDate endDate);

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
}
