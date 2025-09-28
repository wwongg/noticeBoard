package com.example.noticeBoard.service.Impl;

import com.example.noticeBoard.Dto.LoginDto;
import com.example.noticeBoard.Dto.MemberDto;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.MemberRepository;
import com.example.noticeBoard.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username);
        if(member == null) throw new RuntimeException("회원 없음: " + username);

        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);

        memberRepository.save(member);

        log.info("회원 이메일 [{}]의 비밀번호가 변경되었습니다.", username);
    }



    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password((passwordEncoder.encode(memberDto.getPassword())))

                .build();
        return saveEntity(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public boolean login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        Member byUsername = memberRepository.findByUsername(username);
        if (byUsername != null) {
            if(passwordEncoder.matches(password, byUsername.getPassword())) {  // ✅ 암호화 비교
                return true;
            }
        }

        return false;
    }




}
