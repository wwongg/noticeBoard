package com.example.noticeBoard.service.Impl;


import com.example.noticeBoard.Exception.BoardException;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.repository.BoardRepository;
import com.example.noticeBoard.service.BoardSortService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardSortServiceImpl implements BoardSortService {

    private final BoardRepository boardRepository;

    private static final String SORT_TYPE_LIKE = "likeType";
    private static final String SORT_TYPE_LAST_BOARD = "lastBoardType";
    private static final String SORT_TYPE_MANY_COUNT = "manyCountType";
    private static final String SORT_TYPE_MANY_VIEW = "manyViewType";

    @Override
    public Page<Board> TypeSort(String type, Pageable pageable) {
        Page<Board> boards = null;
        switch (type) {
            case SORT_TYPE_LIKE:
                boards = likeSortDesc(pageable);
                return boards;
            case SORT_TYPE_LAST_BOARD:
                boards = lasBoardSortDesc(pageable);
                return boards;
            case SORT_TYPE_MANY_COUNT:
                boards = CommentSortDesc(pageable);
                return boards;
            case SORT_TYPE_MANY_VIEW:
                boards = viewSortDesc(pageable);
                return boards;
            default:
                throw new BoardException("정렬 오류 발생");
        }
    }

    @Override
    public String TypeValue(String typeValue) {
        String result = "";
        switch (typeValue) {
            case SORT_TYPE_LIKE:
                result = "좋아요순";
                return result;
            case SORT_TYPE_LAST_BOARD:
                result = "최신글순";
                return result;
            case SORT_TYPE_MANY_COUNT:
                result = "많은 댓글순";
                return result;
            case SORT_TYPE_MANY_VIEW:
                result = "조회수순";
                return result;
            default:
                throw new BoardException("정렬 오류발생");
        }
    }

    @Override
    public Page<Board> likeSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByBoardLikeDesc(pageable);
    }

    @Override
    public Page<Board> viewSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByViewsDesc(pageable);
    }

    @Override
    public Page<Board> CommentSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByCountDesc(pageable);
    }

    @Override
    public Page<Board> lasBoardSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByDateTimeDesc(pageable);
    }
}
