package org.lecture.chatservice.dto;

public record ChatMessage(
        String sender,
        String message,
        String time
) {
}
