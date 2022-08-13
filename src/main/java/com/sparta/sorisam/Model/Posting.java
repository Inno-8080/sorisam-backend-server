package com.sparta.sorisam.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posting")
public class Posting extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postingId;

    @Column(nullable = false)
    private String title;

    //받아올것
    @Column(nullable = false)
    private String img;

    //받아올것
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Long cntPostLike;

}
