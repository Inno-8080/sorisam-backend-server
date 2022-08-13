package com.sparta.sorisam.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
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

}
