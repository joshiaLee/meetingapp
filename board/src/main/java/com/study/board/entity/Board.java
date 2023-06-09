package com.study.board.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private int count;

    private String title;

    @Column(length = 10000)
    private String content;

    private String filename;

    private String filepath;

    private String memberName;

    private Long member_id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private List<Comment> comments = new ArrayList<>();


}
