package org.lecture.chatservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.ChatroomDto;
import org.lecture.chatservice.entity.Chatroom;
import org.lecture.chatservice.repository.ChatroomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultantService {

    private final ChatroomRepository chatroomRepository;

    public Page<ChatroomDto> getAllChatroom(Pageable pageable) {
        Page<Chatroom> chatroomPage = chatroomRepository.findAll(pageable);

        return chatroomPage.map(ChatroomDto::from);
    }
}
