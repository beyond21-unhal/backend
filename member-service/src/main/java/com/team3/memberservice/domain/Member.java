package com.team3.memberservice.domain;


import com.team3.memberservice.common.BaseEntity;
import com.team3.memberservice.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public Member(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}

