package com.study.board.service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.*;
import com.study.board.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.study.board.entity.QImageEntity.*;

@Service
@Transactional
public class MemberService {

    @Autowired private MemberRepository memberRepository;



    public void join(Member member){

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> selectAll(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member login(Member member){
        Optional<Member> ByMember = memberRepository.findByEmail(member.getEmail());

        if(ByMember.isPresent()){
          // 아이디 있는 경우
            Member findMember = ByMember.get();

            if(findMember.getPassword().equals(member.getPassword())){ // 비번이 같으면
                return findMember;
            } else{
                return null;
            }

        } else{
            // 아이디 없는 경우
            return null; // 실패
        }
    }


    @Transactional(readOnly = true)
    public Member findByEmail(String email){
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isPresent()){
            return byEmail.get();
        }
        else{
            return null;
        }
    }


    @Transactional(readOnly = true)
    public Member findMatching(Member loginMember) {

        double latitude = loginMember.getCoord().get_lat();
        double longitude = loginMember.getCoord().get_lng();
        double distance = 10;

        List<Member> members = memberRepository.doMatching(loginMember);

        if (!members.isEmpty()) {
            int randomIndex = new Random().nextInt(members.size());
            Member randomMember = members.get(randomIndex);

            return randomMember;
        } else {
            return null;
        }
    }
}
