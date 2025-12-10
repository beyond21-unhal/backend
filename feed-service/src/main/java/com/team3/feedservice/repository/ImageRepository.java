package com.team3.feedservice.repository;

import com.team3.feedservice.domain.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<FeedImage, Long> {

}
