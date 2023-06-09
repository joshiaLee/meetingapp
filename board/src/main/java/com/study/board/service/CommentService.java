package com.study.board.service;

import com.study.board.entity.Comment;
import com.study.board.entity.Member;
import com.study.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {


    @Autowired CommentRepository commentRepository;

    public void join(Comment comment){

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id){
         return commentRepository.findById(id).get();
    }

    public void delete(Comment comment){
        commentRepository.delete(comment);
    }
}
