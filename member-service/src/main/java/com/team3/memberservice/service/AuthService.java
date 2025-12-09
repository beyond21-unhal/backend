package com.team3.memberservice.service;

import com.amuraedo.modulecommon.dto.response.ApiResponse;
import com.amuraedo.modulecommon.util.JwtUtil;
import com.team3.memberservice.domain.Member;
import com.team3.memberservice.dto.request.LoginDTO;
import com.team3.memberservice.dto.request.SignupDTO;
import com.team3.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponse<?> createUser(SignupDTO signupDTO){
        boolean check = memberRepository.findMemberByUsername(signupDTO.id()).isPresent();
        if(check){
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .username(signupDTO.id())
                .password(passwordEncoder.encode(signupDTO.password()))
                .role(signupDTO.role())
                .build();

        Long id = memberRepository.save(member).getUserId();

        String accessToken = jwtUtil.createToken(id, String.valueOf(member.getRole()));

        return ApiResponse.success(accessToken);
    }

    public ApiResponse<?> login(LoginDTO loginDTO) {
// 1. 아이디 검증 (없으면 에러)
        Member member = memberRepository.findMemberByUsername(loginDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        // 혹은 new CustomException(ErrorCode.MEMBER_NOT_FOUND)

        // 2. ⭐ 비밀번호 검증 (반드시 matches 사용!)
        if (!passwordEncoder.matches(loginDTO.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 발급
        String accessToken = jwtUtil.createToken(member.getUserId(), String.valueOf(member.getRole()));

        return ApiResponse.success(accessToken);
    }

}
