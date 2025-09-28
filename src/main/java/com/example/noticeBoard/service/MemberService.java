package com.example.noticeBoard.service;

import com.example.noticeBoard.Dto.BoardLikeDto;
import com.example.noticeBoard.Dto.MemberDto;
import com.example.noticeBoard.entity.Member;

import java.util.List;

public interface MemberService {
    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);



  //  Member findByBoardId(String boardId);


}
