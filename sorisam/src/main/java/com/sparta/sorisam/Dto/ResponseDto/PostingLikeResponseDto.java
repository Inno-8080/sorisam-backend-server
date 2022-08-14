package com.sparta.sorisam.Dto.ResponseDto;

import lombok.Getter;

@Getter
public class PostingLikeResponseDto {
    private Long postingId;
    private Long cntPostLike;

    public PostingLikeResponseDto(Long postingId, Long cntPostLike) {
        this.postingId = postingId;
        this.cntPostLike = cntPostLike;
    }
}
