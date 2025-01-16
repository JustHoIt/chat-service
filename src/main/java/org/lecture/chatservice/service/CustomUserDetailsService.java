package org.lecture.chatservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lecture.chatservice.dto.MemberDto;
import org.lecture.chatservice.entity.Member;
import org.lecture.chatservice.enums.Role;
import org.lecture.chatservice.repository.MemberRepository;
import org.lecture.chatservice.vo.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).get();

        if (Role.fromCode(member.getRole()) != Role.CONSULTANT) {
            throw new AccessDeniedException("상담사가 아닙니다.");
        }

        return new CustomUserDetails(member, null);
    }

    public MemberDto saveMember(MemberDto dto) {
        Member member = MemberDto.to(dto);
        member.updatePassword(dto.password(), dto.confirmedPassword(), passwordEncoder);

        member = memberRepository.save(member);

        return MemberDto.from(member);
    }
}
