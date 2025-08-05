package com.example.noticeBoard.service;

import com.example.noticeBoard.entity.Member;

public interface BoardLikeService {

    boolean isBoardCheck(Member loginMember, Long boardId);

    void deleteByBoardId(Long boardId);
}
