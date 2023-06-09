package com.study.board.repositorycustom;

import com.study.board.entity.ImageEntity;

import java.util.List;


public interface ImageEntityRepositoryCustom {


    List<ImageEntity> saveImages(Long member_id);

    void deleteByMemberId(Long member_id);
}
