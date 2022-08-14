package com.sparta.sorisam.Repository;

import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.PostingLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostingLike, Long> {

    Optional<PostingLike> findByUsernameAndPosting(String username, Posting posting);

    Long countByPosting(Posting posting);
    List<PostingLike> findAllByPosting(Posting posting);
}
