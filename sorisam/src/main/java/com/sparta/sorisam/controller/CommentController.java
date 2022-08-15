package com.sparta.sorisam.controller;

import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.global.common.response.ApiUtils;
import com.sparta.sorisam.global.common.response.CommonResponse;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.security.UserDetailsImpl;
import com.sparta.sorisam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postingId}")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment") //모든 댓글 조회 (대댓글 포함)
    public List<Comment> readAllComment(@PathVariable Long postingId) {
        return commentService.getAllComments(postingId);
    }

    @GetMapping("/comment/{commentId}") //댓글 조회 (대댓글 포함)
    public Comment readComment(@PathVariable Long postingId, @PathVariable Long commentId) {
        return commentService.getComment(postingId, commentId);
    }

    @PostMapping("/comment") //댓글 생성
    public CommonResponse<?> createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.commentCreate(requestDto, postingId);
        return ApiUtils.success(200, "댓글이 등록되었습니다.");
    }

    @PutMapping("/comment/{commentId}") //댓글 수정
    public CommonResponse<?> updateComment(@RequestBody CommentRequestDto requstDto, @PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.commentUpdate(requstDto, postingId, commentId, userDetails.getUsername());
        return ApiUtils.success(200, "댓글이 수정되었습니다.");
    }

    @DeleteMapping("/comment/{commentId}") //댓글 삭제
    public CommonResponse<?> deleteComment(@PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.commentDelete(commentId, userDetails.getUsername());
        return ApiUtils.success(200, "댓글이 삭제되었습니다.");
    }

    @PostMapping("/comment/{commentId}/recomment") //대댓글 생성
    public CommonResponse<?> createRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.recommentCreate(requestDto, commentId);
        return ApiUtils.success(200, "대댓글이 등록되었습니다.");
    }

    @PutMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 수정
    public CommonResponse<?> updateRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.recommentUpdate(requestDto, postingId, commentId, recommentId, userDetails.getUsername());
        return ApiUtils.success(200, "대댓글이 수정되었습니다.");
    }

    @DeleteMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 삭제
    public CommonResponse<?> deleteRecomment(@PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        commentService.recommentDelete(commentId, recommentId, userDetails.getUsername());
        return ApiUtils.success(200, "대댓글이 삭제되었습니다.");
    }

}
