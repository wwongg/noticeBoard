package com.example.noticeBoard.service;

import com.example.noticeBoard.Dto.CommentDto;
import com.example.noticeBoard.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment save(CommentDto commentDto);

    List<Comment> findAllComment(Long boardId);



    Integer countComment(Long boardId);

    void updateComment(CommentDto commentDto);

    void deleteComment(CommentDto commentDto);

    void deleteBoardId(Long boardId);
}
