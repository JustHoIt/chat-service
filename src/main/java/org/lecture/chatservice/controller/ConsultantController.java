package org.lecture.chatservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.ChatroomDto;
import org.lecture.chatservice.dto.MemberDto;
import org.lecture.chatservice.service.ConsultantService;
import org.lecture.chatservice.service.CustomUserDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/consultants")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ConsultantController {

    private final CustomUserDetailsService customUserDetailsService;
    private final ConsultantService consultantService;


    @ResponseBody
    @PostMapping
    public MemberDto saveMember(@RequestBody MemberDto memberDto) {
        return customUserDetailsService.saveMember(memberDto);
    }

    @GetMapping
    public String index() {

        return "consultants/index.html";
    }


    @ResponseBody
    @GetMapping("/chats")
    public Page<ChatroomDto> getAllChatroom(Pageable pageable) {

        return consultantService.getAllChatroom(pageable);
    }

}
