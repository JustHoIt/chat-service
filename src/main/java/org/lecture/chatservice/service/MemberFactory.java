package org.lecture.chatservice.service;

import org.lecture.chatservice.entity.Member;
import org.lecture.chatservice.enums.Gender;
import org.lecture.chatservice.enums.Role;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MemberFactory {

    public static Member create(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        return switch (userRequest.getClientRegistration().getRegistrationId()) {

            case "kakao" -> {
                Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");
                yield Member.builder()
                        .email((String) attributeMap.get("email"))
                        .nickName((String) ((Map) attributeMap.get("profile")).get("nickname"))
                        .name((String) attributeMap.get("name"))
                        .phoneNumber((String) attributeMap.get("phone_number"))
                        .gender(Gender.valueOf(((String) attributeMap.get("gender")).toUpperCase()))
                        .birthDay(getBirthDay(attributeMap))
                        .role(Role.USER.getCode())
                        .build();
            }

            case "google" -> {
                Map<String, Object> attributeMap = oAuth2User.getAttributes();
                yield Member.builder()
                        .email((String) attributeMap.get("email"))
                        .nickName((String) attributeMap.get("given_name"))
                        .name((String) attributeMap.get("name"))
                        .role(Role.USER.getCode())
                        .build();
            }

            default ->
                    throw new IllegalStateException("지원하지 않는 Oauth2 서비스입니다.");
        };

    }

    private static LocalDate getBirthDay(Map<String, Object> attributeMap) {
        String birthYear = (String) attributeMap.get("birthyear");
        String birthday = (String) attributeMap.get("birthday");


        return LocalDate.parse(birthYear + birthday, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
