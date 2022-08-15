package com.sparta.sorisam.controller;

import com.sparta.sorisam.Dto.RequestDto.PostingRequestDto;
import com.sparta.sorisam.Dto.RequestDto.PostingUpdateRequestDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingDetailResponseDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingResponseDto;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.global.common.response.ApiUtils;
import com.sparta.sorisam.global.common.response.CommonResponse;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.security.UserDetailsImpl;
import com.sparta.sorisam.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostingController {

    private final PostingService postingService;
    private final PostingRepository postingRepository;

    // 게시글 작성
    @PostMapping
    public CommonResponse<?> createPosting(@RequestBody PostingRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 ID의 username

        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        String username = userDetails.getUsername();
        String img = userDetails.getUserImg();
        String intro = userDetails.getUserIntro();
        postingService.createPosting(requestDto, username, img, intro);
        return ApiUtils.success(200, "게시글이 등록되었습니다.");
    }

    // 게시글 조회
    @GetMapping
    public List<PostingResponseDto> getPosting() {
        return postingService.getPosting();
    }

    // 게시글 디테일 조회
    @GetMapping("/{id}")
    public PostingDetailResponseDto getDetailPosting(@PathVariable Long id) {
        return postingService.getDetailPosting(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public CommonResponse<?> updatePosting(@PathVariable long id, @RequestBody PostingUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        String username = userDetails.getUsername();
        requestDto.setUsername(username);

        postingService.updatePosting(id, requestDto, username);
        return ApiUtils.success(200, "게시글이 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public CommonResponse<?> deletePosting(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        String username = userDetails.getUsername();
        postingService.deletePosting(id,username);
        return ApiUtils.success(200, "게시글이 삭제되었습니다.");
    }

}
