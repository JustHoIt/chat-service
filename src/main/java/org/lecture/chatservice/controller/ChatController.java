package org.lecture.chatservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.entity.Chatroom;
import org.lecture.chatservice.service.ChatService;
import org.lecture.chatservice.vo.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {


    private final ChatService chatService;

    @PostMapping
    public Chatroom createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody String title) {

        return chatService.createChatroom(user.getMember(), title);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId) {

        return chatService.joinChatroom(chatroomId, user.getMember());
    }


    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId) {

        return chatService.leaveChatroom(chatroomId, user.getMember());
    }

    @GetMapping
    public List<Chatroom> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {

        return chatService.getChatroomList(user.getMember());
    }
}
