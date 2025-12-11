package com.team3.feedservice.controller;

import com.team3.feedservice.dto.request.FeedCreateDTO;
import com.team3.feedservice.dto.request.FeedUpdateDTO;
import com.team3.feedservice.dto.response.FeedResponseDTO;
import com.team3.feedservice.service.FeedService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 작성 API입니다.")
//    @ApiResponse(responseCode = "201", description = "피드 생성 성공",
//            content = @Content(mediaType = "application/json",
//                    examples = @ExampleObject(
//                            name = "피드 생성 성공",
//                            value = "{ \"feedId\": 1, \"feedTitle\": \"오늘 운동\", \"feedContent\": \"가슴운동\" }"
//                    )))

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<FeedResponseDTO> createFeed(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestPart("feed") FeedCreateDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(feedService.createFeed(dto, userId, image));
    }

    @Operation(summary = "제목으로 피드검색 API입니다.")
    @GetMapping("/search/title")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<FeedResponseDTO>> findByTitle(@RequestParam String feedTitle) {
        return ResponseEntity.ok(feedService.getFeedsByTitle(feedTitle));
    }

    @Operation(summary = "내용으로 피드검색 API입니다.")
    @GetMapping("/search/content")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<FeedResponseDTO>> findByContent(@RequestParam String feedContent) {
        return ResponseEntity.ok(feedService.getFeedsByContent(feedContent));
    }

    @Operation(summary = "사용자ID로 피드검색 API입니다.")
    @GetMapping("/search/user")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<FeedResponseDTO>> findByUserId(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {
        return ResponseEntity.ok(feedService.getFeedsByUserId(userId));
    }

    @Operation(summary = "피드 수정 API입니다.")
    @PatchMapping(value = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<FeedResponseDTO> updateFeed(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @PathVariable Long feedId,
            @RequestPart("feed") FeedUpdateDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile newImage) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(feedService.updateFeed(feedId, dto, newImage, userId));
    }

    @Operation(summary = "피드 삭제 API입니다.")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{feedId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> delete(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @PathVariable Long feedId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        feedService.deleteFeed(feedId, userId);
        return ResponseEntity.noContent().build(); // 204 반환
    }

}
