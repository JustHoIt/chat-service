package org.lecture.chatservice.dto;

import org.lecture.chatservice.entity.Chatroom;

import java.time.LocalDateTime;

public record ChatroomDto(
        Long id,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        Boolean hasNewMessage) {

    public static ChatroomDto from(Chatroom chatroom) {
        return new ChatroomDto(chatroom.getId(), chatroom.getTitle(),
                chatroom.getMemberChatroomMappingSet().size(), chatroom.getCreatedAt(), chatroom.getHasNewMessage());
    }
}
