package org.lecture.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.lecture.chatservice.repository.MemberRepository;
import org.lecture.chatservice.entity.Member;
import org.lecture.chatservice.enums.Gender;
import org.lecture.chatservice.vo.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");
        String email = (String) attributeMap.get("email");
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> registerMember(attributeMap));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

    private Member registerMember(Map<String, Object> attributeMap) {
        Member member = Member.builder()
                .email((String) attributeMap.get("email"))
                .nickName((String) ((Map) attributeMap.get("profile")).get("nickname"))
                .name((String) attributeMap.get("name"))
                .phoneNumber((String) attributeMap.get("phone_number"))
                .gender(Gender.valueOf(((String)attributeMap.get("gender")).toUpperCase()))
                .birthDay(getBirthDay(attributeMap))
                .role("USER_ROLE")
                .build();

        return memberRepository.save(member);
    }

    private LocalDate getBirthDay(Map<String, Object> attributeMap) {
        String birthYear = (String) attributeMap.get("birthyear");
        String birthday = (String) attributeMap.get("birthday");


        return LocalDate.parse(birthYear + birthday, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
