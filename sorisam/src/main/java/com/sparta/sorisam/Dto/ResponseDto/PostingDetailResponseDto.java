package com.sparta.sorisam.Dto.ResponseDto;

import com.sparta.sorisam.Model.Posting;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostingDetailResponseDto {
    // 게시글 디테일 페이지
    private String username;
    private String img;
    private String intro;

    private Long postingId;
    private String title;
    private String contents;
    private String filePath;
    private Long cntPostLike;

    @Builder
    public PostingDetailResponseDto(Posting posting, Long cntPostLike) {
        this.username = posting.getUsername();
        this.img = posting.getImg();
        this.intro = posting.getIntro();
        this.postingId = posting.getPostingId();
        this.title = posting.getTitle();
        this.contents = posting.getContents();
        this.filePath = posting.getFilePath();
        this.cntPostLike = cntPostLike;
    }


}
