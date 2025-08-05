package com.example.noticeBoard.service;

import com.example.noticeBoard.Dto.BoardDto;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    Board save(BoardDto boardDto);

    Board findByBoardId(Long boardID);

    Board updateBoard(BoardDto boardDto);
    Integer passwordVerify(Long boardId, String password, String username);

    boolean deleteBoard(Long boardId, String memberUsername);

    Page<Board> search(String title, Pageable pageable);

    int board_like(BoardDto boardDto);

    Board countViews(Long boardId);

    List<Board> findAll();
}
