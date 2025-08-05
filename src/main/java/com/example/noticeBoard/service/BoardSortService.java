package com.example.noticeBoard.service;

import com.example.noticeBoard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSortService {

    Page<Board> TypeSort(String type, Pageable pageable);

    String TypeValue(String typeValue);

    Page<Board> likeSortDesc(Pageable pageable);

    Page<Board> viewSortDesc(Pageable pageable);

    Page<Board> CommentSortDesc(Pageable pageable);

    Page<Board> lasBoardSortDesc(Pageable pageable);
}
