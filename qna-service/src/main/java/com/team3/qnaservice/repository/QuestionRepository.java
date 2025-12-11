package com.team3.qnaservice.repository;

import com.team3.qnaservice.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase(
            String titleKeyword, String contentKeyword);

    List<Question> findQuestionsByUserId(Long userId);

    // 트레이너용
    List<Question> findByIsAnsweredFalse();
    List<Question> findByIsAnsweredFalseAndQuestionContentContaining(String keyword);
}
