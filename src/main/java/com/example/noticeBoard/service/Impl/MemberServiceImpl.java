package com.example.noticeBoard.service.Impl;

import com.example.noticeBoard.Dto.LoginDto;
import com.example.noticeBoard.Dto.MemberDto;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.MemberRepository;
import com.example.noticeBoard.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
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
            if(byUsername.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }




}
