package com.study.board.repository;

import com.study.board.entity.ImageEntity;
import com.study.board.repositorycustom.ImageEntityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, Long>, ImageEntityRepositoryCustom {



}
