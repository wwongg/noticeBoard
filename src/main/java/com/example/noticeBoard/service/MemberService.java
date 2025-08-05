package com.example.noticeBoard.service;

import com.example.noticeBoard.Dto.MemberDto;
import com.example.noticeBoard.entity.Member;

public interface MemberService {
    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);

  //  Member findByBoardId(String boardId);


}
