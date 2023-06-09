package com.study.board.service;


import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;


    public void join(Board board){
        boardRepository.save(board);
    }
    //글 작성
    public void write(Board board, MultipartFile file) throws Exception{


        if(!file.isEmpty()) {
            String projectPath = "/home/lks9909/file/";

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "." + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath(projectPath + fileName);
        }

        boardRepository.save(board);
    }

    //게시글 리스트 처리
    @Transactional(readOnly = true)
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    @Transactional(readOnly = true)
    public Board boardView(Long id){
        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Long id){
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
