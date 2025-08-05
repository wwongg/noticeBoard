package com.example.noticeBoard.service.Impl;

import com.example.noticeBoard.Dto.BoardDto;
import com.example.noticeBoard.Exception.Login_RestException;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Board_Like_check;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.BoardLikeRepository;
import com.example.noticeBoard.repository.BoardRepository;
import com.example.noticeBoard.service.BoardService;
import com.example.noticeBoard.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j  // 이를 사용하여 로깅을 구현하게 되면 추후에 필요로 의해 로깅 라이브러리르 변경할 때 코드의 변경 없이 가능하다.
@RequiredArgsConstructor    // 생성자를 안 만들어도 자동으로 만들어줌
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final BoardLikeRepository boardLikeRepository;

    @Override
    public Board save(BoardDto boardDto) {

        String username = boardDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);
        Board build = Board.builder().
                title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(byUsername.getUsername())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
                .count(0)
                .member(byUsername)
                .views(0)
                .boardLike(0)
                .build();

        Board save = boardRepository.save(build);
        return save;

    }

    @Override
    public Board findByBoardId(Long boardId) {
        try {
            return boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("Board not found with ID: " + boardId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding Board by ID", e);
        }
    }

    @Override
    public Board updateBoard(BoardDto boardDto) {

        Board byBoardId = findByBoardId(boardDto.getId());
        byBoardId.update(boardDto.getTitle(), boardDto.getContent());
        return boardRepository.save(byBoardId);
    }

    @Override
    public Board countViews(Long boardId) {
        Board byBoardId = findByBoardId(boardId);
        Integer views = byBoardId.getViews();
        byBoardId.setViews(++views);
        return boardRepository.save(byBoardId);
    }

    @Override
    public Integer passwordVerify(Long boardId, String password, String username) {
        Board byBoardId = findByBoardId(boardId);
        String boardPassword = byBoardId.getPassword();
        Member memberUsername = memberService.findByUsername(username);

        if (boardPassword.equals(password)) {
            if (memberUsername == null || !(byBoardId.getWriter().equals(username))) {
                return 2;
            }
            return 1;
        }
        return 0;
    }

    @Override
    public boolean deleteBoard(Long boardId, String memberUsername) {
        Member byUsername = memberService.findByUsername(memberUsername);
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            String writer = boardOptional.get().getWriter();
            if (byUsername != null && writer.equals(memberUsername)) {
                boardRepository.deleteById(boardId);
                return true;
            }
        } else {
            throw new EntityNotFoundException("게시물이 존재하지 않습니다. ID: " + boardId);
        }
        return false;
    }

    @Override
    public Page<Board> search(String title, Pageable pageable) {
        return boardRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public int board_like(BoardDto boardDto) {
        Long boardDtoId = boardDto.getId();
        String username = boardDto.getMemberDto().getUsername();

        Board board = findByBoardId(boardDtoId);
        Member member = memberService.findByUsername(username);

        if(member != null) {
            Board_Like_check byMemberId = boardLikeRepository.findMemberId(member.getId(), board.getId());


            if (byMemberId != null && byMemberId.getBoard().getId().equals(board.getId())) {
                Integer currentLike = board.getBoardLike();
                if(currentLike == null) currentLike = 0;
                board.setBoardLike(currentLike - 1);
                boardRepository.save(board);
                boardLikeRepository.deleteBoard_Id(board.getId());
                return -1;
            } else {
                Integer currentLike = board.getBoardLike();
                if(currentLike == null) currentLike = 0;
                board.setBoardLike(currentLike + 1);
                boardRepository.save(board);

                Board_Like_check boardLike = Board_Like_check.builder()
                        .like_check(true)
                        .member(member)
                        .board(board)
                        .boardLike(board.getBoardLike())    // null 방지
                        .build();

                boardLikeRepository.save(boardLike);
                return 1;
            }
        } else {
            throw new Login_RestException("로그인을 한 후에 이용할 수 있습니다.");
        }
    }


    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board entitySave(Board board) {
        return boardRepository.save(board);
    }

}
