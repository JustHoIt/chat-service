package org.lecture.chatservice.repository;

import org.lecture.chatservice.entity.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChatroomMappingRepository extends JpaRepository<MemberChatroomMapping, Long> {
    boolean existsByMemberIdAndChatroomId(Long id, Long chatroomId);

    void deleteByMemberIdAndChatroomId(Long id, Long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long id);
}
