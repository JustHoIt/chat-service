package org.lecture.chatservice.dto;

import org.lecture.chatservice.entity.Member;
import org.lecture.chatservice.enums.Gender;

import java.time.LocalDate;

public record MemberDto(
        Long id,
        String email,
        String nickName,
        String name,
        String password,
        String confirmedPassword,
        Gender gender,
        String phoneNumber,
        LocalDate birthDay,
        String role) {

    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getName(),
                null,
                null,
                member.getGender(),
                member.getPhoneNumber(),
                member.getBirthDay(),
                member.getRole()
        );
    }

    public static Member to(MemberDto dto) {
        return Member.builder()
                .id(dto.id())
                .email(dto.email())
                .nickName(dto.nickName())
                .name(dto.name())
                .gender(dto.gender())
                .phoneNumber(dto.phoneNumber())
                .birthDay(dto.birthDay())
                .role(dto.role())
                .build();
    }
}
