package com.study.board.entity;

import com.study.board.embed.Coord;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Data
public class Member{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;


    private Integer age;

    private String sex; // 성별 [MAN, WOMAN]

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(length = 1000)
    private String introduction;

    @Embedded
    private Coord coord;

    private String address;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<ImageEntity> imageEntities = new ArrayList<>();



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Message> messages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "idlist", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "member_id_list")
    private Set<Long> id_list = new HashSet<>();


}
