package com.example.noticeBoard.service;

import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Member;

import java.util.List;

public interface BoardLikeService {

    boolean isBoardCheck(Member loginMember, Long boardId);

    void deleteByBoardId(Long boardId);

    List<Board> findLikedBoards(Member member);
}
