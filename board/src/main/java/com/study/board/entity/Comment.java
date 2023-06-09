package com.study.board.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String memberName;

    private String password;
    private Long member_id;

    public Comment(String content, String password, String memberName, Long member_id) {
        this.content = content;
        this.memberName = memberName;
        this.member_id = member_id;
        this.password = password;
    }

    public Comment() {

    }
}
