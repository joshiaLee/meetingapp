package com.study.board.repositorycustom;

import com.study.board.entity.Message;

import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> findingMessageFromMemberId(Long toM, Long fromM);
}
