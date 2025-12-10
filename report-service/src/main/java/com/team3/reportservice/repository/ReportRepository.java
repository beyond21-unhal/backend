package com.team3.reportservice.repository;

import com.team3.reportservice.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByUserIdAndStartDate(Long userId, LocalDate startDate);
}
