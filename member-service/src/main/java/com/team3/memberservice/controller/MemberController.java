package com.team3.memberservice.controller;

import com.team3.memberservice.dto.request.SignupDTO;
import com.team3.memberservice.service.MemberService;
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
    public ResponseEntity<?> getInformation(@RequestHeader("X-User-Id") Long userId, @RequestHeader("X-User-Role") String role) {

        log.info("membercontroller - userid {}, role {}", userId, role);
        return ResponseEntity.ok(memberService.getInformation(userId));
    }


}
