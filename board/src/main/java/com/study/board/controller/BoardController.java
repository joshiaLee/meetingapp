package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.Member;
import com.study.board.service.BoardService;
import com.study.board.service.CommentService;
import com.study.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;

@Slf4j
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam("password") String password,
                                @RequestParam("board_id") Long board_id, Model model){


        Comment comment = commentService.findById(id);

        if(!comment.getPassword().equals(password)){
            model.addAttribute("message", "비밀번호가 다릅니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + board_id);
            return "message";
        }

        commentService.delete(comment);

        model.addAttribute("message", "댓글이 삭제 되었습니다.");
        model.addAttribute("searchUrl", "/board/view?id=" + board_id);

        return "message";



    }


    @GetMapping("/board/write") // localhost:8080/board/write
    public String boardWriteForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        model.addAttribute("loginMember", loginMember);
        return "boardwrite";
    }



    @PostMapping("/board/writePro")
    public String boardWritePro(Board board, MultipartFile file , Model model) throws Exception{
        boardService.write(board, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id",
                                    direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){

        Page<Board> list = null;

        if(searchKeyword == null){
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }


        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4,1);
        int endPage =Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list); // 리턴값이 "list"로 넘어간다
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Long id){
        Board board = boardService.boardView(id);
        board.setCount(board.getCount() + 1);
        boardService.join(board);

        model.addAttribute("board", board);
        return "boardview";
    }

    // 댓글 달기
    @PostMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardViewComment(@ModelAttribute Comment comment, Long id, Model model, HttpServletRequest request){

        HttpSession session = request.getSession();

        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Board currentBoard = boardService.boardView(id);

        Comment newComment = new Comment(comment.getContent(), comment.getPassword(), sessionMember.getUsername() ,sessionMember.getId());

        currentBoard.getComments().add(newComment);

        boardService.join(currentBoard);


        model.addAttribute("board", currentBoard);
        return "boardview";
    }

    //
    @GetMapping("board/delete")
    public String boardDelete(Long id, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();

        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Board currentBoard = boardService.boardView(id);


        if(sessionMember.getId() == currentBoard.getMember_id()) {
            boardService.boardDelete(id);
            return "redirect:/board/list";
        }

        else{
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
            return "message";
        }
    }



    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id,
                              Model model, HttpServletRequest request){

        HttpSession session = request.getSession();

        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Board currentBoard = boardService.boardView(id);


        if(sessionMember.getId() == currentBoard.getMember_id()) {
            model.addAttribute("board", boardService.boardView(id));
            return "boardmodify";
        }

        else{
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
            return "message";
        }



    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id,
                              Board board, Model model, MultipartFile file) throws Exception{
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    // 이미지 보기(이거 없으면 액박뜸)
    @GetMapping("/images/{filename}")
    public @ResponseBody Resource downloadImage(@PathVariable String filename) throws MalformedURLException {


        String projectPath = "/home/lks9909/file/";
        // file:/home/lks9909/~ /498739487ff-2199bc-49ac-a033-91803poi.png

        return new UrlResource("file:" + projectPath + filename);
    }


}
