package com.sparta.sorisam.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.sorisam.Dto.RequestDto.PostingRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "posting")
@Builder
@AllArgsConstructor
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


    public Posting(PostingRequestDto requestDto, String username, String img, String intro) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.filePath = requestDto.getFilePath();
        this.username = username;
        this.img = img;
        this.intro = intro;
    }

    public Posting(String title, String contents, String filePath, String username, String img, String intro) {
        this.title = title;
        this.contents = contents;
        this.filePath = filePath;
        this.username = username;
        this.img = img;
        this.intro = intro;
    }


    public void updatePosting(String title, String contents, String filePath) {
        this.title = title;
        this.contents = contents;
        this.filePath = filePath;
    }

}
