package com.study.board.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Data
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    private Long toMember;
    private Long member_id;
}
