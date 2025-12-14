package com.team3.memberservice.service;

import com.team3.memberservice.domain.Member;
import com.team3.memberservice.dto.response.ApiResponse;
import com.team3.memberservice.dto.response.UserInfoDTO;
import com.team3.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ApiResponse<?> getInformation(Long userId) {

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("member not found")
        );

        return ApiResponse.success(
                UserInfoDTO.builder()
                        .userId(member.getUserId())
                        .username(member.getUsername())
                        .build()
        );

    }
}
