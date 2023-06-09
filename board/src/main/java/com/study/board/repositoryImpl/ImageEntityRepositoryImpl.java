package com.study.board.repositoryImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.ImageEntity;
import com.study.board.repositorycustom.ImageEntityRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.board.entity.QImageEntity.imageEntity;


@Repository
@RequiredArgsConstructor
public class ImageEntityRepositoryImpl implements ImageEntityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ImageEntity> saveImages(Long member_id) {
        return queryFactory
                .selectFrom(imageEntity)
                .where(imageEntity.member_id.eq(member_id))
                .fetch();
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory.delete(imageEntity)
                .where(imageEntity.member_id.eq(memberId));
    }
}
