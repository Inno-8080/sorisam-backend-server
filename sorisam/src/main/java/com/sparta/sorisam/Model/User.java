package com.sparta.sorisam.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;

    @Column(nullable = false)
    private String password;
    //사진
    String img;
    //자기소개
    String intro;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
