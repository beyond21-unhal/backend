package com.team3.dietplanservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DietPlanImage {
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
    public DietPlanImage(String originalFileName, String fileName, String fileContent) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
