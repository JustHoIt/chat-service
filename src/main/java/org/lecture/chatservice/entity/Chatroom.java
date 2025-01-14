package org.lecture.chatservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Chatroom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "chatroom_id")
    Long id;

    String title; //채팅방 제목

    LocalDateTime createdAt; //채팅방 생성시간

    LocalDateTime lastChatAt; //마지막 채팅시간

    @OneToMany(mappedBy = "chatroom")
    Set<MemberChatroomMapping> memberChatroomMappingSet = new HashSet<>();

    public MemberChatroomMapping addMember(Member member) {
        if (this.getMemberChatroomMappingSet() == null) {
            this.memberChatroomMappingSet = new HashSet<>();
        }

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberChatroomMappingSet.add(memberChatroomMapping);

        return memberChatroomMapping;
    }
}
