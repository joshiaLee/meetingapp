package com.study.board.repositoryImpl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.Member;
import com.study.board.repositorycustom.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.board.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> doMatching(Member loginMember) {

        double latitude = loginMember.getCoord().get_lat();
        double longitude = loginMember.getCoord().get_lng();
        double distance = 10;

        return queryFactory.select(member)
                .from(member)
                .where(
                        Expressions.numberTemplate(Double.class,
                                        "6371 * acos(cos(radians({0})) * cos(radians({2})) * cos(radians({3}) - radians({1})) + sin(radians({0})) * sin(radians({2})))",
                                        member.coord._lat, member.coord._lng, latitude, longitude)
                                .loe(distance),
                        member.id.ne(loginMember.getId()),
                        member.sex.ne(loginMember.getSex()),
                        member.id_list.contains(loginMember.getId()).not()


                )
                .fetch();
    }


}
