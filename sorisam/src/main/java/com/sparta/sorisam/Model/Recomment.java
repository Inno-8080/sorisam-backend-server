package com.sparta.sorisam.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.sorisam.Dto.RequestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "recomment")
public class Recomment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recommentId;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false)
    @JsonIgnore
    private Comment comment;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    public Recomment(Comment comment, String contents) {
        this.comment = comment;
        this.username = ""; //로그인된 유저 정보 받아오기
        this.contents = contents;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
