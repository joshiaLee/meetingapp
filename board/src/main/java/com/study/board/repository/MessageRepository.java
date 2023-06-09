package com.study.board.repository;

import com.study.board.entity.Message;
import com.study.board.repositorycustom.MessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {
    List<Message> findByToMember(Long toMember);


}
