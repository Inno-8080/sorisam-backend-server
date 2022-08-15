package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.Recomment;
import com.sparta.sorisam.Repository.CommentRepository;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.Repository.RecommentRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final UserService userService;



    //모든 댓글 조회
    public List<Comment> getAllComments(Long postingId) {
        return commentRepository.findAllByPosting_PostingId(postingId);
    }

    //특정 댓글 조회
    public Comment getComment(Long postingId, Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT));
    }

    //댓글 생성
    public Comment commentCreate (CommentRequestDto requestDto, Long postingId){
        //댓글이 달릴 게시물
        Posting post = postingRepository.findById(postingId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_POST));

        //댓글 생성해서 내용 저장
        String contents = requestDto.getContents();
        if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        }

        Comment comment = new Comment(post, requestDto.getContents());
        //로그인된 유저 정보 저장
        comment.setUsername(userService.getMyInfo().getUsername());
        //댓글 데이터 저장
        commentRepository.save(comment);

        return comment;
    }

    //댓글 수정
    @Transactional
    public Comment commentUpdate(CommentRequestDto requestDto, Long postingId, Long commentId, String username){
        //수정할 댓글 찾기 (사실 코멘트아이디가 PK 라서 포스팅아이디 필요없음)
        Comment comment = commentRepository.findByPosting_PostingIdAndCommentId(postingId, commentId);
        if(comment == null) {
            throw new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT);
        }
        //댓글 작성자와 로그인된 유저 비교
        if (!comment.getUsername().equals(username)) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        //댓글 정보 업데이트
        String contents = requestDto.getContents();
        if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        }
        comment.update(requestDto);

        return comment;
    }

    //댓글 삭제
    @Transactional
    public Long commentDelete(Long commentId, String username){
        //삭제할 댓글 찾기
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT));
        //댓글 작섣자와 로그인된 유저 비교
        if (!comment.getUsername().equals(username)) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        //댓글 데이터 삭제
        commentRepository.deleteById(commentId);
        return commentId;
    }

    //대댓글 생성
    public Recomment recommentCreate(CommentRequestDto requestDto, Long commentId) {
        //대댓글이 달릴 댓글
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT));
        //대댓글 생성
        String contents = requestDto.getContents();
        if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        }
        Recomment recomment = new Recomment(comment, requestDto.getContents());
        //로그인된 유저 정보 저장
        recomment.setUsername(userService.getMyInfo().getUsername());
        //해당 댓글에 대댓글 정보 추가
        comment.addRecomment(recomment);
        //대댓글 데이터 저장
        recommentRepository.save(recomment);
        return recomment;
    }

    //대댓글 수정
    @Transactional
    public Recomment recommentUpdate(CommentRequestDto requestDto, Long postingId, Long commentId, Long recommentId, String username) {
        //수정할 대댓글이 달린 댓글
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT));
        //수정할 대댓글
        Recomment recomment = recommentRepository.findById(recommentId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTFOUND_RECOMMENT));
        //대댓글 작성자와 로그인된 유저 정보 비교
        if(!recomment.getUsername().equals(username)) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        //대댓글 정보 수정
        String contents = requestDto.getContents();
        if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        }
        recomment.update(requestDto);
        //댓글의 대댓글 정보 수정
        comment.updateRecomment(requestDto, recomment);
        return recomment;
    }

    //대댓글 삭제
    @Transactional
    public Long recommentDelete(Long commentId, Long recommentId, String username) {
        //삭제할 대댓글이 달린 댓글
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_COMMENT));
        //삭제할 댓글
        Recomment recomment = recommentRepository.findById(recommentId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTFOUND_RECOMMENT));
        //대댓글 작성자와 로그인된 유저 정보 비교
        if(!recomment.getUsername().equals(username)) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        //댓글의 해당 대댓글 정보 삭제
        comment.deleteRecomment(recomment);
        //대댓글 데이터 삭제
        recommentRepository.deleteById(recommentId);
        return recommentId;
    }

}
