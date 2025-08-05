package com.example.noticeBoard.controller;


import com.example.noticeBoard.Dto.BoardDto;
import com.example.noticeBoard.Dto.CommentDto;
import com.example.noticeBoard.Exception.*;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Comment;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.event.ViewsEvent;
import com.example.noticeBoard.service.BoardService;
import com.example.noticeBoard.service.CommentService;
import com.example.noticeBoard.service.Impl.BoardLikeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ApplicationEventPublisher eventPublisher;
    private final CommentService commentService;
    private final BoardLikeServiceImpl boardLikeService;

    @GetMapping("/board")
    public String board(
            Model model,
            // 페이징 처리를 위한 Pageable 객체를 매개변수로 받고, 기본적으로 한 페이지당 4개의 항목을 보여주도록 설정
            @PageableDefault(size = 4)Pageable pageable,
            // String title: 검색할 게시글의 제목을 매개변수로 받는다.
            @RequestParam(name = "title",required = false, defaultValue = "") String title,
            HttpSession session
    ) {
        Page<Board> boards = boardService.search(title, pageable);
        List<Board> boardAll = boards.getContent();

        getSession(model, session);

        int currentPage = boards.getPageable().getPageNumber() + 1;
        // 전체 페이지 수를 계산
        int totalPages = boards.getTotalPages();

        // 페이징 처리 시 보일 페이지 수를 설정
        int visiblePages = 3;
        // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
        int startPage = Math.max(1, currentPage - visiblePages / 2);
        // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시하되 전체 페이지 수를 초과하지 않도록 설정
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        if(boardAll != null) {
            model.addAttribute("boardAll", boardAll);
        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", boards);

        return "board/board";
    }

    @GetMapping("/boardWrit")
    public String write(Model model, HttpSession session) {
        getSession(model, session);
        model.addAttribute("board", new BoardDto());
        return "board/writeboard";
    }

    @PostMapping("/boardWrit")
    @ResponseBody
    public ResponseEntity<?> writing(@Valid @RequestBody BoardDto boardDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errorMessage = new HashMap<>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            boardService.save(boardDto);

            return ResponseEntity.ok("성공");
        } catch (Login_RestException e) {
            e.printStackTrace();
            throw new Login_RestException("회원가입한 사용자만 사용가능합니다.");
        }

    }


    @GetMapping("/board/{boardId}")
    public String boardInfo(@PathVariable(name = "boardId") Long boardId, Model model, HttpSession session) {

        Board byBoardID = boardService.findByBoardId(boardId);
        List<Comment> allComment = commentService.findAllComment(boardId);
        eventPublisher.publishEvent(new ViewsEvent(byBoardID));

        String board_write_user = byBoardID.getMember().getUsername();
        Member loginMember = getSession(model, session);

        boolean boardCheck = false;

        if (loginMember != null) {
            boardCheck = boardLikeService.isBoardCheck(loginMember, boardId);
        }

        if(loginMember.getUsername().equals(board_write_user)) {
            model.addAttribute("board", byBoardID);
        } else {
            throw new LoginException("작성자만 수정할 수 있습니다.");
        }

        model.addAttribute("allComment", allComment);
        model.addAttribute("comment", new CommentDto());
        model.addAttribute("board_like_check", boardCheck);
        model.addAttribute("board", byBoardID);

            return "board/boardInfo";

    }

    @GetMapping("/board/update/{id}")
    public String updateGetBoard(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        Board byBoardId = boardService.findByBoardId(id);
        Member loginMember = getSession(model, session);
        String board_write_user = byBoardId.getMember().getUsername();
        if(loginMember.getUsername().equals(board_write_user)) {
            model.addAttribute("board", byBoardId);
        } else {
            throw new LoginException("작성자만 수정할 수 있습니다.");
        }
        return "board/updateBoard";
    }

    @PutMapping("/board/update")
    @ResponseBody
    public ResponseEntity<?> updateBoardAfter(@RequestBody BoardDto boardDto) {
        try {
            Board board = boardService.updateBoard(boardDto);

            return ResponseEntity.ok(board);
        } catch (NotFindPageException e) {
            throw new NotFindPage_RestException("해당 게시물을 찾을 수 없습니다.");
        } catch (Login_RestException e) {
            throw new Login_RestException("작성자만 수정 가능합니다.");
        }
    }

    @PostMapping("/password/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@RequestBody BoardDto boardDto) {
        try {
            String password = boardDto.getPassword();
            String dtoUsername = boardDto.getMemberDto().getUsername();
            Long boardId = boardDto.getId();

            Integer integer = boardService.passwordVerify(boardId, password, dtoUsername);

            if(integer == 2) {
                throw new Login_RestException(
                );
            } else if(integer == 1) {
                return ResponseEntity.ok(integer);
            }
            throw new BadRequestException();

        } catch (Login_RestException e) {
            throw new Login_RestException("작성자만 삭제 가능합니다.");
        } catch (BadRequestException e) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        } catch (NotFindPageException e) {
            throw new NotFindPage_RestException("해당 게시물이 더 이상 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> boardDelete(@RequestBody BoardDto boardDto) {
        try {
            Long id = boardDto.getId();
            String memberUsername = boardDto.getMemberDto().getUsername();
            boolean deleteBoard = boardService.deleteBoard(id, memberUsername);
            if(!deleteBoard) {
                throw new Login_RestException();
            }
            return ResponseEntity.ok("성공");
        } catch (NotFindPageException e) {
            throw new NotFindPage_RestException("게시글이 존재하지 않습니다.");
        } catch (Login_RestException e) {
            throw new Login_RestException("작성자만 삭제할 수 있습니다.");
        }
    }

    @PostMapping("/board/likes")
    @ResponseBody
    public ResponseEntity<?> boardLikes(@RequestBody BoardDto boardDto) {
        try {
            log.info("boardDto={}",boardDto);
            Integer likeCount = boardService.board_like(boardDto);
            int num = (likeCount != null) ? likeCount : 0;
            return ResponseEntity.ok(num);
        } catch (Login_RestException e) {
            e.printStackTrace();
            throw new Login_RestException("로그인 후 이용할 수 있습니다.");
        }
    }



    private static Member getSession(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        return loginMember;
    }
}
