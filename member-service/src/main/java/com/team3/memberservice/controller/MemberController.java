package com.team3.memberservice.controller;

import com.team3.memberservice.dto.request.SignupDTO;
import com.team3.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "내 정보")
    public ResponseEntity<?> getInformation(@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId, @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {

        log.info("membercontroller - userid {}, role {}", userId, role);
        return ResponseEntity.ok(memberService.getInformation(userId));
    }


}
