package com.team3.memberservice.service;

import com.team3.memberservice.domain.Member;
import com.team3.memberservice.dto.request.LoginDTO;
import com.team3.memberservice.dto.request.SignupDTO;
import com.team3.memberservice.dto.response.ApiResponse;
import com.team3.memberservice.enums.UserRole;
import com.team3.memberservice.repository.MemberRepository;
import com.team3.memberservice.util.JwtUtil;
import com.team3.memberservice.util.RedisKeyUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    /**
     * 테스트용 Member 객체를 생성하는 헬퍼 메서드
     * userId는 DB에서 생성되므로 Builder에 없어서 Reflection으로 주입
     */
    private Member createMember() {
        Member member = Member.builder()
                .username("testId")
                .password("encodedPassword") // DB에는 암호화된 비밀번호가 저장됨
                .role(UserRole.USER)         // UserRole 열거형 사용 (프로젝트에 정의된 값 사용)
                .build();

        // protected 필드인 userId에 강제로 1L 값을 주입
        ReflectionTestUtils.setField(member, "userId", 1L);
        return member;
    }

    @Test
    @DisplayName("회원가입 성공")
    void createUser_Success() {
        // given
        SignupDTO signupDTO = new SignupDTO("testId", "rawPassword", UserRole.USER);
        Member savedMember = createMember();

        // 1. 중복 체크 통과 설정
        given(memberRepository.findMemberByUsername(signupDTO.id())).willReturn(Optional.empty());
        // 2. 비밀번호 암호화 설정
        given(passwordEncoder.encode(signupDTO.password())).willReturn("encodedPassword");
        // 3. 저장 시 ID가 부여된 Member 반환 설정
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);
        // 4. 토큰 생성 설정
        given(jwtUtil.createToken(savedMember.getUserId(), String.valueOf(savedMember.getRole())))
                .willReturn("accessToken");

        // when
        ApiResponse<?> response = authService.createUser(signupDTO);

        // then
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.data()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void createUser_Fail_Duplicate() {
        // given
        SignupDTO signupDTO = new SignupDTO("testId", "rawPassword", UserRole.USER);

        // 이미 존재하는 멤버가 있다고 가정
        given(memberRepository.findMemberByUsername(signupDTO.id())).willReturn(Optional.of(createMember()));

        // when & then
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class,
                () -> authService.createUser(signupDTO));

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 아이디입니다.");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LoginDTO loginDTO = new LoginDTO("testId", "rawPassword");
        Member member = createMember();

        // 1. ID 조회 성공 설정
        given(memberRepository.findMemberByUsername(loginDTO.id())).willReturn(Optional.of(member));
        // 2. 비밀번호 일치 설정 (rawPassword vs encodedPassword)
        given(passwordEncoder.matches(loginDTO.password(), member.getPassword())).willReturn(true);
        // 3. 토큰 발급 설정
        given(jwtUtil.createToken(member.getUserId(), String.valueOf(member.getRole()))).willReturn("accessToken");

        // when
        ApiResponse<String> response = authService.login(loginDTO);

        // then
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.data()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("로그인 실패 - 아이디 없음")
    void login_Fail_IdNotFound() {
        // given
        LoginDTO loginDTO = new LoginDTO("wrongId", "password");
        given(memberRepository.findMemberByUsername(loginDTO.id())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginDTO));

        assertThat(exception.getMessage()).isEqualTo("가입되지 않은 아이디입니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_WrongPassword() {
        // given
        LoginDTO loginDTO = new LoginDTO("testId", "wrongPassword");
        Member member = createMember();

        given(memberRepository.findMemberByUsername(loginDTO.id())).willReturn(Optional.of(member));
        // 비밀번호 불일치 설정
        given(passwordEncoder.matches(loginDTO.password(), member.getPassword())).willReturn(false);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginDTO));

        assertThat(exception.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() {
        // given
        String accessToken = "Bearer validTokenString";
        String pureToken = "validTokenString";
        long expiration = 3600L;

        // 1. 토큰 만료시간 조회 Mocking
        given(jwtUtil.getExpiration(pureToken)).willReturn(expiration);
        // 2. Redis opsForValue() 호출 시 Mock 객체 반환
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        boolean result = authService.logout(accessToken);

        // then
        assertThat(result).isTrue();

        // Redis에 저장 로직이 호출되었는지 검증
        verify(valueOperations).set(
                RedisKeyUtil.getBlackListKey(pureToken),
                "logout",
                expiration,
                TimeUnit.MILLISECONDS
        );
    }
}