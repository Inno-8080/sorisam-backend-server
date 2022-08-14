package com.sparta.sorisam.Dto.RequestDto;

import com.sparta.sorisam.Model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostingRequestDto {

    private String title;

    private String contents;

    private String filePath;
//
//    private User user;
}
