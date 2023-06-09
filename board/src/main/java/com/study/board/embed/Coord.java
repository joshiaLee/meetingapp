package com.study.board.embed;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter @Setter
public class Coord {


    private double _lat;
    private double _lng;
}
