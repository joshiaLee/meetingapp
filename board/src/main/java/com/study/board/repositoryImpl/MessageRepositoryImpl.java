package com.study.board.repositoryImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.Message;
import com.study.board.repositorycustom.MessageRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.board.entity.QMessage.message;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Message> findingMessageFromMemberId(Long toM, Long fromM) {
        return queryFactory.selectFrom(message)
                .where(message.toMember.eq(toM),
                        message.member_id.eq(fromM))
                .fetch();
    }
}
