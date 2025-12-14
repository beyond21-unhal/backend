package com.team3.feedservice.domain;

import com.team3.feedservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fileContent;

    @Builder
    public FeedImage(String originalFileName, String fileName, String fileContent) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
