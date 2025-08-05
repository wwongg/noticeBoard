package com.example.noticeBoard.controller;


import com.example.noticeBoard.Dto.CommentDto;
import com.example.noticeBoard.Exception.CommentException;
import com.example.noticeBoard.Exception.Login_RestException;
import com.example.noticeBoard.Exception.NotFindPageException;
import com.example.noticeBoard.Exception.NotFindPage_RestException;
import com.example.noticeBoard.entity.Comment;
import com.example.noticeBoard.service.Impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor

public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/api/board/comment")
    public ResponseEntity<?> comment(@RequestBody CommentDto commentDto) {
        try {
            Comment saveComment = commentService.save(commentDto);
            return ResponseEntity.ok(saveComment);
        } catch (Login_RestException e) {
            throw new Login_RestException("로그인을 하지 않았습니다.");
        } catch (NotFindPageException e) {
            throw new NotFindPage_RestException("게시글이 존재하지 않습니다.");
        }
    }

    @PostMapping("/board/comment/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.updateComment(commentDto);
            return ResponseEntity.ok("게시물 수정 성공");
        } catch (CommentException e) {
            throw new CommentException("해당 댓글이 존재하지 않습니다.");
        } catch (Login_RestException e) {
            throw new Login_RestException("작성자만 수정할 수 있습니다.");
        }
    }

    @DeleteMapping("/api/delete/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.deleteComment(commentDto);
            return ResponseEntity.ok("삭제 성공");
        }catch (CommentException e) {
            e.printStackTrace();
            throw new CommentException("댓글 작성자만이 삭제 가능합니다.");
        } catch (Login_RestException e) {
            throw new Login_RestException("로그인을 하지 않음");
        } catch (NotFindPage_RestException e) {
            e.printStackTrace();
            throw new NotFindPage_RestException("해당 댓글이 존재하지 않습니다.");
        }
    }
}
