package com.sparta.sorisam.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String recomment;

}
