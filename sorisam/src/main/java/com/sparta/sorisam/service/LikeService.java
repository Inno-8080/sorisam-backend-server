package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.ResponseDto.PostingLikeResponseDto;
import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.PostingLike;
import com.sparta.sorisam.Model.User;
import com.sparta.sorisam.Repository.LikeRepository;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.Repository.UserRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final UserRepository userRepository;


    // 좋아요 등록 및 취소
    @Transactional
    public PostingLikeResponseDto postingLike(String username, Long postingId) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new InvalidValueException(ErrorCode.NOT_AUTHORIZED)
        );
        Posting posting = postingRepository.findById(postingId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_POST)
        );

        // 게시글 좋아요 여부 확인
        PostingLike findPostLike = likeRepository.findByUsernameAndPosting(username, posting).orElse(null);

        if (findPostLike == null) {
            likeRepository.save(new PostingLike(username, posting));
        } else {
            likeRepository.deleteById(findPostLike.getPostinglikeId());
        }
        return new PostingLikeResponseDto(postingId, likeRepository.countByPosting(posting));
    }


}
