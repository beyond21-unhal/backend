package com.team3.dietplanservice.repository;

import com.team3.dietplanservice.domain.DietPlanImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<DietPlanImage, Long> {

}
