package com.sparta.sorisam.Repository;

import com.sparta.sorisam.Model.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {

}
