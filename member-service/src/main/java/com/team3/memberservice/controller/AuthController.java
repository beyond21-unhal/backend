package com.team3.memberservice.controller;

import com.team3.memberservice.dto.request.LoginDTO;
import com.team3.memberservice.dto.request.SignupDTO;
import com.team3.memberservice.dto.response.ApiResponse;
import com.team3.memberservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> createMember(@RequestBody SignupDTO  signupDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(signupDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    @SecurityRequirement(name="JWT")
    public ResponseEntity<ApiResponse<String>> logout(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        if(authService.logout(token)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
