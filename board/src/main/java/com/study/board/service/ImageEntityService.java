package com.study.board.service;

import com.study.board.entity.ImageEntity;
import com.study.board.repository.ImageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageEntityService {

    @Autowired private ImageEntityRepository imageEntityRepository;

    public void deleteByMemberId(Long memberId) {
        imageEntityRepository.deleteByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<ImageEntity> saveImages(Long member_id){
        return imageEntityRepository.saveImages(member_id);
    }


}
