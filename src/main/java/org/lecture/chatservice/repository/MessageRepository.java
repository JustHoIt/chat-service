package org.lecture.chatservice.repository;

import org.lecture.chatservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatroomId(Long chatroomId);
}
