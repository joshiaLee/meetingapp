package com.study.board.repositorycustom;


import com.study.board.entity.Member;

import java.util.List;


public interface MemberRepositoryCustom {

    List<Member> doMatching(Member member);


}
