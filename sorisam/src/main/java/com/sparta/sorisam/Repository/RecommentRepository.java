package com.sparta.sorisam.Repository;

import com.sparta.sorisam.Model.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {
    Recomment findByRecommentId(Long recommentId);
    //Recomment findByComment_Posting_PostingIdAndComment_CommentIdAndRecommentId(Long postingId, Long commentId, Long recommentId);
}
