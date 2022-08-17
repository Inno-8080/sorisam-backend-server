package com.sparta.sorisam.Dto.RequestDto;

import com.sparta.sorisam.Model.Posting;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class PostingRequestDto {

    private String title;

    private String contents;

    private MultipartFile audioFile;

    private String filePath;

}
