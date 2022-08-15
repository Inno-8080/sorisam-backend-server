package com.sparta.sorisam.controller;

import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.Model.Recomment;
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
    public Comment createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.commentCreate(requestDto, postingId);
    }

    @PutMapping("/comment/{commentId}") //댓글 수정
    public Comment updateComment(@RequestBody CommentRequestDto requstDto, @PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.commentUpdate(requstDto, postingId, commentId, userDetails.getUsername());
    }

    @DeleteMapping("/comment/{commentId}") //댓글 삭제
    public Long deleteComment(@PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.commentDelete(commentId, userDetails.getUsername());
    }

    @PostMapping("/comment/{commentId}/recomment") //대댓글 생성
    public Recomment createRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.recommentCreate(requestDto, commentId);
    }

    @PutMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 수정
    public Recomment updateRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.recommentUpdate(requestDto, postingId, commentId, recommentId, userDetails.getUsername());
    }

    @DeleteMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 삭제
    public Long deleteRecomment(@PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인 여부 확인
        if (userDetails == null) {
            throw new InvalidValueException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
        return commentService.recommentDelete(commentId, recommentId, userDetails.getUsername());
    }

}
