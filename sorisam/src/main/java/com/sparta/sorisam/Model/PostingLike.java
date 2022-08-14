package com.sparta.sorisam.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "postingLikes") //테이블 이름으로 괜찮은지?
@NoArgsConstructor
@Getter
@Setter
public class PostingLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postinglikeId;

    @ManyToOne(targetEntity = Posting.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "postingId", nullable = false)
    private Posting posting;

    @Column(nullable = false)
    private String username;

    public PostingLike(String username, Posting posting) {
        this.username = username;
        this.posting = posting;
    }

}
