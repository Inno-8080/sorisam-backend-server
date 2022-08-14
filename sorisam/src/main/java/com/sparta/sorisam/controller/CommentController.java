package com.sparta.sorisam.controller;

import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import com.sparta.sorisam.Dto.RequestDto.CommentResponseDto;
import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.Model.Recomment;
import com.sparta.sorisam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postingId}")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment") //모든 댓글 조회
    public List<Comment> readAllComment(@PathVariable Long postingId) {
        return commentService.getAllComments(postingId);
    }

    @GetMapping("/comment/{commentId}") //댓글 조회 (대댓글 포함)
    public Comment readComment(@PathVariable Long postingId, @PathVariable Long commentId) {
        return commentService.getComment(postingId, commentId);
    }

    @PostMapping("/comment") //댓글 생성
    public Comment createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId) {
        return commentService.commentCreate(requestDto, postingId);
    }

    @PutMapping("/comment/{commentId}") //댓글 수정
    public Comment updateComment(@RequestBody CommentRequestDto requstDto, @PathVariable Long postingId, @PathVariable Long commentId){
        return commentService.commentUpdate(requstDto, postingId, commentId);
    }

    @DeleteMapping("/comment/{commentId}") //댓글 삭제
    public Long deleteComment(@PathVariable Long postingId, @PathVariable Long commentId){
        return commentService.commentDelete(commentId);
    }

    @PostMapping("/comment/{commentId}/recomment") //대댓글 생성
    public Recomment createRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId) {
        return commentService.recommentCreate(requestDto, commentId);
    }

    @PutMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 수정
    public Recomment updateRecomment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId) {
        return commentService.recommentUpdate(requestDto, postingId, commentId, recommentId);
    }

    @DeleteMapping("/comment/{commentId}/recomment/{recommentId}") //대댓글 삭제
    public Long deleteRecomment(@PathVariable Long postingId, @PathVariable Long commentId, @PathVariable Long recommentId){
        return commentService.recommentDelete(commentId, recommentId);
    }

}
