package com.example.noticeBoard.repository;


import com.example.noticeBoard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long CommentId);

    @Query("select c from Comment c where c.board.Id = :boardId")
    List<Comment> findByBoard_Id(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(c) from Comment c where c.board.Id = :id")
    Integer countComment(@Param(("id")) Long boardId);

    @Modifying
    @Query("delete from Comment c where c.board.Id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
