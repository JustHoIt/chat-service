package org.lecture.chatservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.ChatMessage;
import org.lecture.chatservice.entity.Message;
import org.lecture.chatservice.service.ChatService;
import org.lecture.chatservice.vo.CustomOAuth2User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handlerMessage(@AuthenticationPrincipal Principal principal, @DestinationVariable Long chatroomId, @Payload Map<String, String> payload) {
        log.info("{} sent {} in {}", principal.getName(), payload, chatroomId);
        CustomOAuth2User user = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        Message message = chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");

        return new ChatMessage(principal.getName(), payload.get("message"), message.getCreatedAt().format(formatter));
    }

}
