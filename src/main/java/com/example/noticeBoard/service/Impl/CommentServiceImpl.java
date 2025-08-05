package com.example.noticeBoard.service.Impl;

import com.example.noticeBoard.Dto.CommentDto;
import com.example.noticeBoard.Exception.CommentException;
import com.example.noticeBoard.Exception.Login_RestException;
import com.example.noticeBoard.Exception.NotFindPage_RestException;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Comment;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.CommentRepository;
import com.example.noticeBoard.service.CommentService;
import com.example.noticeBoard.service.MemberService;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardServiceImpl boardService;
    private final MemberService memberService;

    @Override
    public Comment save(CommentDto commentDto) {
        Long boardId = commentDto.getBoardDto().getId();
        String username = commentDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);

        Board byBoardId = boardService.findByBoardId(boardId);

        if(byUsername == null) {
            throw new Login_RestException();
        }

        Comment build = Comment.builder()
                .content(commentDto.getComment())
                .dateTime(LocalDateTime.now())
                .member(byUsername)
                .board(byBoardId).build();

        Comment save = commentRepository.save(build);
        setComment(byBoardId.getId());
        return save;
    }

    @Override
    public List<Comment> findAllComment(Long boardId) {
        List<Comment> byBoardId = commentRepository.findByBoard_Id(boardId);
        return byBoardId;
    }

    @Override
    public Integer countComment(Long boardId) {
        try {
            return commentRepository.countComment(boardId);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("값이 나오지 않았습니다.");
        }
    }

    @Override
    public void updateComment(CommentDto commentDto) {
        Long id = commentDto.getId();
        String username = commentDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->new CommentException("댓글 수정시 Id 오류"));

        if(byUsername == null || !comment.getMember().getUsername().equals(byUsername.getUsername())) {
            throw new Login_RestException("사용자 인증 오류");
        }

        comment.setContent(commentDto.getComment());
        comment.setUpdateDateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(CommentDto commentDto) {
        String dtoUsername = commentDto.getMemberDto().getUsername();
        String username = memberService.findByUsername(dtoUsername).getUsername();

        Long commentDtoId = commentDto.getId();
        Optional<Comment> byId = commentRepository.findById(commentDtoId);
        if(byId.isPresent()) {
            Long commentId = byId.get().getId();
            String commentWriter = byId.get().getMember().getUsername();

            if (!username.equals(commentWriter)) {
                throw new CommentException("댓글 지우기 로직 오류");
            }
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFindPage_RestException("댓글 지우기 로직 오류: " + commentDtoId + "값이 없음");
        }
    }

    @Override
    public void deleteBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }


    private Board setComment(Long boardId) {
        Integer countComment = countComment(boardId);
        Board byBoardId = boardService.findByBoardId(boardId);
        byBoardId.setCount(countComment);
        return boardService.entitySave(byBoardId);
    }
}
