package com.sparta.sorisam.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.sorisam.Dto.RequestDto.PostingRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "posting")
public class Posting extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String filePath;

    //받아올것
    @Column(nullable = false)
    private String username;

    @Column
    private String img;

    @Column
    private String intro;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"posting"})
    private List<PostingLike> postingLikeList = new ArrayList<>();

//    @Column
//    private Long cntPostLike = 0L;

//    @Builder
//    public Posting(String username, String title, String img, String filePath, String contents){
//        this.username = username;
//        this.title = title;
//        this.img = img;
//        this.filePath = filePath;
//        this.contents = contents;
//    }

    public Posting(PostingRequestDto requestDto, String username, String img, String intro) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.filePath = requestDto.getFilePath();
        this.username = username;
        this.img = img;
        this.intro = intro;
    }

    public void updatePosting(String title, String filePath, String contents) {
        this.title = title;
        this.filePath = filePath;
        this.contents = contents;
    }

}
