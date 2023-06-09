package com.study.board.entity;

import com.study.board.embed.UploadFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "IMAGE")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    private Long member_id;



}
