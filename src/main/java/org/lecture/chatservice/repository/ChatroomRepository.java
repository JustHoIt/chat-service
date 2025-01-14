package org.lecture.chatservice.repository;

import org.lecture.chatservice.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
