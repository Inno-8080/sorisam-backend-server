package com.sparta.sorisam.Dto.RequestDto;

import com.sparta.sorisam.Model.Comment;
import com.sparta.sorisam.Model.Recomment;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CommentResponseDto {

    private Long postingId;
    private Long commentId;
    private String username;
    private String contents;
    private Long cntLike;
    private List<Recomment> recomments;

}
