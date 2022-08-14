package com.sparta.sorisam.Dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostingResponseDto {

    private String username;
    private String img;

    private Long postingId;
    private String title;
    private String filePath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    private Long cntPostLike;

    @Builder
    public PostingResponseDto(Posting posting, Long cntPostLike) {
        this.username = posting.getUsername();
        this.img = posting.getImg();
        this.postingId = posting.getPostingId();
        this.title = posting.getTitle();
        this.filePath = posting.getFilePath();
        this.modifiedAt = posting.getModifiedAt();
        this.createdAt = posting.getCreatedAt();
        this.cntPostLike = cntPostLike;
    }
}
