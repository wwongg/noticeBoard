package com.example.noticeBoard.controller;

import com.example.noticeBoard.Exception.BoardException;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.service.BoardSortService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardSortController {
    private final BoardSortService boardSortService;

    @GetMapping("/board/sortType")
    public String likeSon(@RequestParam(value = "sortBoard", defaultValue = "", required = false) String sortType,
                          Model model,
                          HttpSession session,
                          @PageableDefault Pageable pageable){
        try {
            Member loginMember = (Member) session.getAttribute("loginMember");

            Page<Board> boards = boardSortService.TypeSort(sortType, (org.springframework.data.domain.Pageable) pageable);
            List<Board> content = boards.getContent();
            String typeValue = boardSortService.TypeValue(sortType);

            int currentPage = boards.getPageable().getPageNumber() + 1;
            int totalPages = boards.getTotalPages();
            int visiblePages = 3;
            int startPage = Math.max(1, currentPage - visiblePages / 2);
            int endPage = Math.min(totalPages, startPage + visiblePages - 1);

            model.addAttribute("typeValue",typeValue);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("sortBoardList", content);
            model.addAttribute("page", boards);
            model.addAttribute("sortBoard", sortType);
            model.addAttribute("loginMember", loginMember);

            return "board/sortBoard";
        }catch (BoardException e){
            e.printStackTrace();
            return "redirect:/board";
        }catch (Exception e){
            log.error("Exception occur",e);
            return "redirect:/error-500";
        }
        }
    }


