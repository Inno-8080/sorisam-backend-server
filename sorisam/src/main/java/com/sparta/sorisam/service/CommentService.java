package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import com.sparta.sorisam.Dto.RequestDto.CommentResponseDto;
import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.Recomment;
import com.sparta.sorisam.Repository.CommentRepository;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.Repository.RecommentRepository;
import jdk.jfr.consumer.RecordedMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;


    //모든 댓글 조회
    public List<Comment> getAllComments(Long postingId) {
        return commentRepository.findAllByPosting_PostingId(postingId);
    }

    //특정 댓글 조회
    public Comment getComment(Long postingId, Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    //댓글 생성
    public Comment commentCreate (CommentRequestDto requestDto, Long postingId){
        //게시물 조회
        Posting post = postingRepository.findById(postingId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다")
        );

        // 새 댓글 생성해서 저장하고
        Comment comment = new Comment(post, requestDto.getContents());
        //comment.confirmPost(post);
        commentRepository.save(comment);

        return comment;
    }

    //댓글 수정
    @Transactional
    public Comment commentUpdate(CommentRequestDto requestDto, Long postingId, Long commentId){

        Comment comment = commentRepository.findByPosting_PostingIdAndCommentId(postingId, commentId);

//        if (comment == null) {
//            throw new IllegalArgumentException("삭제할 권한이 없습니다");
//        }

        comment.update(requestDto);

        return comment;
    }

    //댓글 삭제
    @Transactional
    public Long commentDelete(Long commentId){

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다"));

        if (!Objects.equals(comment.getCommentId(), commentId)) {
            throw new IllegalArgumentException("삭제하실 권한이 없습니다");
        }
        commentRepository.deleteById(commentId);
        return commentId;
    }

    //대댓글 생성
    public Recomment recommentCreate(CommentRequestDto requestDto, Long commentId) {
         Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다")
         );
         Recomment recomment = new Recomment(comment, requestDto.getContents());
         comment.addRecomment(recomment);
         recommentRepository.save(recomment);
         return recomment;
    }

    //대댓글 수정
    @Transactional
    public Recomment recommentUpdate(CommentRequestDto requestDto, Long postingId, Long commentId, Long recommentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다")
        );
        Recomment recomment = recommentRepository.findByRecommentId(recommentId);

        comment.updateRecomment(requestDto, recomment);
        recomment.update(requestDto);
        return recomment;
    }

    //대댓글 삭제
    @Transactional
    public Long recommentDelete(Long commentId, Long recommentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다")
        );
        Recomment recomment = recommentRepository.findById(recommentId).orElseThrow(() -> new IllegalArgumentException("해당하는 대댓글이 없습니다"));

        if (!Objects.equals(recomment.getRecommentId(), recommentId)) {
            throw new IllegalArgumentException("삭제하실 권한이 없습니다");
        }
        comment.deleteRecomment(recomment);
        recommentRepository.deleteById(recommentId);
        return recommentId;
    }

}
