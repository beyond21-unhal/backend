package com.team3.feedservice.repository;

import com.team3.feedservice.domain.Image2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image2, Long> {

}
