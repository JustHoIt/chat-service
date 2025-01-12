package org.lecture.chatservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lecture.chatservice.enums.Gender;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Id
    Long id;

    String email;
    String nickName;
    String name;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String phoneNumber;
    LocalDate birthDay;
    String role;
}
