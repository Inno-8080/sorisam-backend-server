package com.sparta.sorisam.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "postingId", nullable = false)
    @JsonIgnore
    private Posting posting;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Long cntLike;

    // comment recomment 연관 관계 설정
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recomment> recomments = new ArrayList<>();

    public Comment(Posting post, String contents) {
        this.posting = post;
        this.username = "로그인된유저"; //로그인된 유저 정보 받아오기
        this.contents = contents;
        this.cntLike = 0L;
        this.recomments = new ArrayList<>();
    }
    public void confirmPost(Posting post) {
        this.posting = post;
        //post.addCommentlist(this);
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void addRecomment(Recomment recomment) {
        this.recomments.add(recomment);
    }

    public void updateRecomment(CommentRequestDto requestDto, Recomment recomment) {
        Recomment newRecomment = recomment;
        newRecomment.setContents(requestDto.getContents());
        this.recomments.set(this.recomments.indexOf(recomment), newRecomment);
    }

    public void deleteRecomment(Recomment recomment) {
        this.recomments.remove(this.recomments.indexOf(recomment));
    }


//    public Comment(Posting posting, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
//        this.posting = posting;
//        this.comment = requestDto.getComment();
//        this.username = userDetails.getUsername();
//        this.cntLike = 0L;
//
//    }

}