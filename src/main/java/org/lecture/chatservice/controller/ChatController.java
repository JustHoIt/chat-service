package org.lecture.chatservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.ChatMessage;
import org.lecture.chatservice.dto.ChatroomDto;
import org.lecture.chatservice.entity.Chatroom;
import org.lecture.chatservice.entity.Message;
import org.lecture.chatservice.service.ChatService;
import org.lecture.chatservice.vo.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {


    private final ChatService chatService;

    @PostMapping
    public ChatroomDto createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestParam String title) {
        Chatroom chatroom = chatService.createChatroom(user.getMember(), title);

        return ChatroomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId, @RequestParam(required = false) Long currentChatroomId) {

        return chatService.joinChatroom(chatroomId, user.getMember(), currentChatroomId);
    }


    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId) {

        return chatService.leaveChatroom(chatroomId, user.getMember());
    }

    @GetMapping
    public List<ChatroomDto> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {
        List<Chatroom> chatroomList = chatService.getChatroomList(user.getMember());

        return chatroomList.stream()
                .map(ChatroomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessageList(@PathVariable Long chatroomId) {

        List<Message> messageList = chatService.getMessageList(chatroomId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");

        return messageList.stream()
                .map(message -> new ChatMessage(message.getMember().getNickName(), message.getText(),
                        message.getCreatedAt().format(formatter))).collect(Collectors.toList());
    }
}
