package com.team3.notificationservice.controller;

import com.team3.notificationservice.dto.request.NotificationDTO;
import com.team3.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController // API 요청을 처리하는 컨트롤러임을 명시 (이거 없으면 일반 클래스 취급됨)
@RequestMapping("/notification")
public class NotificationController {

    // 서비스 가져오기 RequiredArgsConstructor 필수 집가서 이해
    private final NotificationService notificationService;

    @PostMapping("/notification")
    @Tag(name = "postMapping", description = "사용자에게 새로운 알림을 생성합니다. 알림 내용, 수신자 ID, 발신자 ID 등을 전달하면 저장된 알림 ID가 반환됩니다")
    @SecurityRequirement(name="JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> notification(@RequestBody NotificationDTO dto){

        // 컨트롤러가 직접 변환 X 서비스한테 저장
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(dto));
    }

    @PatchMapping("/notification/{id}")
    @Tag(name = "patchMapping", description = "알림을 사용자가 확인했음을 표시하기 위해 checkNotification 값을 true 로 변경합니다. 이미 읽음 처리된 알림은 동일한 상태로 유지됩니다")
    @SecurityRequirement(name="JWT")
    public ResponseEntity<Void> statusUpdate(@PathVariable Long id ){
        notificationService.updateCheck(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notification/{id}")
    @Tag(name = "DeleteMapping", description = "사용자의 요청에 따라 특정 알림을 완전히 삭제합니다. 삭제된 알림은 복구할 수 없으며, 클라이언트는 204 No Content 응답을 받습니다")
    @SecurityRequirement(name="JWT")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id){
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}