package org.lecture.chatservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.ChatMessage;
import org.lecture.chatservice.dto.ChatroomDto;
import org.lecture.chatservice.entity.Message;
import org.lecture.chatservice.service.ChatService;
import org.lecture.chatservice.vo.CustomOAuth2User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handlerMessage(@AuthenticationPrincipal Principal principal, @DestinationVariable Long chatroomId, @Payload Map<String, String> payload) {
        log.info("{} sent {} in {}", principal.getName(), payload, chatroomId);
        CustomOAuth2User user = (CustomOAuth2User) ((AbstractAuthenticationToken) principal).getPrincipal();
        Message message = chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        messagingTemplate.convertAndSend("/sub/chats/updates", chatService.getChatroom(chatroomId));

        return new ChatMessage(principal.getName(), payload.get("message"), message.getCreatedAt().format(formatter));
    }

}
