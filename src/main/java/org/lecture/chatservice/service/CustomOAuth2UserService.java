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

        String email = oAuth2User.getAttribute("email");
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = MemberFactory.create(oAuth2User, userRequest);
                    return memberRepository.save(newMember);
                });

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

}
