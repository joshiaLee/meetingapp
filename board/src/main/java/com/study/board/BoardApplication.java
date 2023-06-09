package com.study.board;

import com.study.board.entity.Member;
import com.study.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.persistence.Temporal;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class BoardApplication {

	@Autowired
	MemberService memberService;

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}


	@PostConstruct
	public void init(){
		List<Member> members = memberService.selectAll();
		for (Member member : members) {
			member.setId_list(new HashSet<>());
			memberService.join(member);
		}
	}


}
