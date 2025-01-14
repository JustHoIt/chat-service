package org.lecture.chatservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberChatroomMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_chatroom_mapping_id")
    Long id;


    @JoinColumn(name = "member_id")
    @ManyToOne
    Member member;

    @JoinColumn(name = "chatroom_id")
    @ManyToOne
    Chatroom chatroom;
}
