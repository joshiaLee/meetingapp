package com.study.board.controller;

import com.study.board.common.ImageForm;
import com.study.board.common.MyStaticId;
import com.study.board.embed.UploadFile;
import com.study.board.entity.*;
import com.study.board.file.FileStore;
import com.study.board.service.ImageEntityService;
import com.study.board.service.MemberService;
import com.study.board.service.MessageService;
import com.study.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
public class MemberController {

    @Autowired private MemberService memberService;
    @Autowired private FileStore fileStore;

    @Autowired private ImageEntityService imageService;

    @Autowired private MessageService messageService;

    @Autowired private ImageEntityService imageEntityService;

    @Autowired private MyStaticId myStaticId;

    @PostMapping("/member/email-check")
    public @ResponseBody String checkEmail(@RequestParam("email") String email){
        Member byEmail = memberService.findByEmail(email);
        if(byEmail != null){
            return null;
        }
        else{
            return "ok";
        }

    }

    @PostMapping("/member/upload_image")
    public @ResponseBody List<UploadFile> saveItem(@ModelAttribute ImageForm form) throws IOException {

        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());


        return storeImageFiles;

    }

    @GetMapping("/member/my")
    public String myPage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        List<ImageEntity> imageEntities = imageEntityService.saveImages(loginMember.getId());

        loginMember.setImageEntities(imageEntities);

        model.addAttribute("member", loginMember);
        return "my";
    }

    @GetMapping("/member/save")
    public String memberSaveForm(){
        return "signup";
    }

    @PostMapping("/member/save")
    public @ResponseBody String memberSave(@RequestBody Member member){
        memberService.join(member);
        return "ok";

    }

    @GetMapping("/member/modify")
    public String boardGetModify(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        model.addAttribute("member", loginMember);
        return "membermodify";
    }

    @PostMapping("/member/modify")
    public @ResponseBody String boardPostModify(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @RequestBody Member member, Model model){

        loginMember.setUsername(member.getUsername());
        loginMember.setIntroduction(member.getIntroduction());
        loginMember.setAge(member.getAge());
        loginMember.setSex(member.getSex());
        loginMember.setCoord(member.getCoord());
        loginMember.setAddress(member.getAddress());
        loginMember.setPassword(member.getPassword());

        if(member.getImageEntities() != null) {
            loginMember.setImageEntities(member.getImageEntities());
            imageService.deleteByMemberId(loginMember.getId());
        }

        memberService.join(loginMember);

        return "/board/list";
    }



    @PostMapping("/member/login")
    public @ResponseBody String login(@RequestBody Member member, HttpServletRequest request){
        Member loginMember = memberService.login(member);

        if(loginMember != null) {
            // 로그인 성공
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            return "ok";
        }
        else {
            // 실패
            return null;
        }
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        myStaticId.id = null;

        return "redirect:/";
    }



    @GetMapping("/member/matching")
    public String matchService(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model){

        Member matchedMember = memberService.findMatching(loginMember);

        if(matchedMember != null) {

            matchedMember.getId_list().add(loginMember.getId());
            memberService.join(matchedMember);

            myStaticId.id = matchedMember.getId();



            return "redirect:/member/view";
        }

        model.addAttribute("message", "만보(10km)이내에 회원이 없습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/member/view")
    public String matchService(Model model){

        if(myStaticId.id == null){
            model.addAttribute("message", "잘못된 접근");
            model.addAttribute("searchUrl", "/board/list");

            return "message";
        }

        Member findMember = memberService.findById(myStaticId.id).get();

        model.addAttribute("member", findMember);


        return "matching";
    }



    @PostMapping("/member/view")
    public String matchMessage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                               @ModelAttribute Message message
            ,Model model){



        message.setMember_id(loginMember.getId());

        messageService.join(message);

        model.addAttribute("message", "쪽지를 보냈습니다.");
        model.addAttribute("searchUrl", "/member/view");


        return "message";
    }

    @PostMapping("/member/view2")
    public String matchService2(@ModelAttribute Member member, Model model){
        myStaticId.id = member.getId();

        log.info("member={}", member);

        Member findMember = memberService.findById(myStaticId.id).get();

        model.addAttribute("member", findMember);


        return "matching";
    }




    @GetMapping("/member/mymessages")
    public String myMessages(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            Model model){


        List<Message> messages = messageService.findingMessage(loginMember.getId());

        Map<Long, Member> m = new HashMap<>();


        for (Message message : messages) {
            m.put(message.getMember_id(), memberService.findById(message.getMember_id()).get());
        }


        model.addAttribute("message", messages);
        model.addAttribute("maps", m);
        return "mymessages";
    }

    @GetMapping("/member/mymessage")
    public String myMessage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Long id
                            ,Model model){

        List<Message> messages = messageService.findingMessageFromMemberId(loginMember.getId(), id);
        Member matching = memberService.findById(id).get();

        model.addAttribute("messages", messages);
        model.addAttribute("member", matching);

        return "mymessage";
    }







//    @GetMapping("/member/clear")
//    public String matchingClear(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
//        loginMember.setId_list(new HashSet<>());
//        memberService.join(loginMember);
//
//        model.addAttribute("message", "매칭이 초기화 되었습니다.");
//        model.addAttribute("searchUrl", "/board/list");
//
//        return "message";
//    }

}
