package com.sparta.sorisam.Dto;

import com.sparta.sorisam.Model.PostingLike;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostingLikeUserDto {
    private String username;

    public PostingLikeUserDto(PostingLike postingLike){
        this.username = postingLike.getUsername();
    }
}
