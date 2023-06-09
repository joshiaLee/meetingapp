package com.study.board.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.Message;
import com.study.board.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.board.entity.QMessage.*;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public void join(Message message){
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> findingMessage(Long memberId){
        return messageRepository.findByToMember(memberId);
    }

    @Transactional(readOnly = true)
    public List<Message> findingMessageFromMemberId(Long toM, Long fromM) {
        return messageRepository.findingMessageFromMemberId(toM, fromM);
    }
}
