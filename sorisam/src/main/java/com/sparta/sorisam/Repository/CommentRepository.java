package com.sparta.sorisam.Repository;

import com.sparta.sorisam.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPosting_PostingId(Long postingId);
    Comment findByPosting_PostingIdAndCommentId(Long postingId, Long commentId);
}
