package com.team3.feedservice.service;

import com.team3.feedservice.domain.Feed;
import com.team3.feedservice.dto.request.FeedCreateDTO;
import com.team3.feedservice.dto.request.FeedUpdateDTO;
import com.team3.feedservice.dto.response.FeedResponseDTO;
import com.team3.feedservice.repository.FeedRepository;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedServiceTests {

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private MinioFileService minioFileService;

    @InjectMocks
    private FeedService feedService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("피드 생성 성공 - 이미지 포함")
    void createFeed_withImage_success() throws Exception {
        // given
        FeedCreateDTO dto = new FeedCreateDTO("제목", "내용");

        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "dummy".getBytes()
        );

        Feed feed = dto.toEntity();
        feed.assignUserId(1L);

        Feed savedFeed = Feed.builder()
                .feedTitle("제목")
                .feedContent("내용")
                .build();
        savedFeed.assignUserId(1L);
        savedFeed.attachImage("http://minio/test.jpg");

        when(minioFileService.saveImageFile(any())).thenReturn("http://minio/test.jpg");
        when(feedRepository.save(any())).thenReturn(savedFeed);

        // when
        FeedResponseDTO result = feedService.createFeed(dto, 1L, image);

        // then
        assertThat(result.feedTitle()).isEqualTo("제목");
        assertThat(result.imageUrl()).isEqualTo("http://minio/test.jpg");
        verify(minioFileService, times(1)).saveImageFile(any());
        verify(feedRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("피드 생성 성공 - 이미지 없음")
    void createFeed_noImage_success() throws Exception {
        FeedCreateDTO dto = new FeedCreateDTO("제목", "내용");
        Feed feed = dto.toEntity();
        feed.assignUserId(1L);

        Feed savedFeed = Feed.builder()
                .feedTitle("제목")
                .feedContent("내용")
                .build();
        savedFeed.assignUserId(1L);

        when(feedRepository.save(any())).thenReturn(savedFeed);

        FeedResponseDTO result = feedService.createFeed(dto, 1L, null);

        assertThat(result.feedTitle()).isEqualTo("제목");
        assertThat(result.imageUrl()).isNull();
        verify(minioFileService, never()).saveImageFile(any());
    }


    @Test
    @DisplayName("피드 수정 성공 - 기존 이미지 교체")
    void updateFeed_changeImage_success() throws Exception {
        // 기존 피드
        Feed feed = Feed.builder()
                .feedTitle("old-title")
                .feedContent("old-content")
                .build();
        feed.assignUserId(1L);
        feed.attachImage("http://minio/old.jpg");

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(minioFileService.saveImageFile(any())).thenReturn("http://minio/new.jpg");

        MockMultipartFile newImage = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", "new".getBytes()
        );

        FeedUpdateDTO dto = new FeedUpdateDTO("new-title", "new-content");

        FeedResponseDTO result = feedService.updateFeed(1L, dto, newImage, 1L);

        assertThat(result.feedTitle()).isEqualTo("new-title");
        assertThat(result.imageUrl()).isEqualTo("http://minio/new.jpg");

        verify(minioFileService, times(1)).deleteImageByUrl("http://minio/old.jpg");
        verify(minioFileService, times(1)).saveImageFile(any());
    }


    @Test
    @DisplayName("피드 수정 실패 - 작성자 불일치")
    void updateFeed_wrongUser_fail() {
        Feed feed = Feed.builder()
                .feedTitle("old-title")
                .feedContent("old-content")
                .build();
        feed.assignUserId(1L);

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));

        FeedUpdateDTO dto = new FeedUpdateDTO("new-title", "new-content");

        assertThatThrownBy(() -> feedService.updateFeed(1L, dto, null, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수정 권한이 없습니다.");
    }


    @Test
    @DisplayName("피드 삭제 성공")
    void deleteFeed_success() throws Exception {
        Feed feed = Feed.builder()
                .feedTitle("title")
                .feedContent("content")
                .build();
        feed.assignUserId(1L);
        feed.attachImage("http://minio/img.jpg");

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));

        feedService.deleteFeed(1L, 1L);

        verify(minioFileService, times(1)).deleteImageByUrl("http://minio/img.jpg");
        verify(feedRepository, times(1)).delete(feed);
    }


    @Test
    @DisplayName("피드 삭제 실패 - 작성자 불일치")
    void deleteFeed_wrongUser_fail() {
        Feed feed = Feed.builder().feedTitle("t").feedContent("c").build();
        feed.assignUserId(1L);

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));

        assertThatThrownBy(() -> feedService.deleteFeed(1L, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인의 피드만 삭제할 수 있습니다.");
    }

}
