package com.sparta.sorisam.controller;

import com.sparta.sorisam.global.common.response.ApiUtils;
import com.sparta.sorisam.global.common.response.CommonResponse;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.security.UserDetailsImpl;
import com.sparta.sorisam.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class LikeController  {
    private final LikeService likeService;

    @PostMapping("/{postingId}/likes")
    public CommonResponse<?> PostingLike(@PathVariable Long postingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        likeService.postingLike(userDetails.getUsername(), postingId);
        return ApiUtils.success(200, null);
    }

}
