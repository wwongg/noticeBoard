package com.example.noticeBoard.repository;

import com.example.noticeBoard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);

    Page<Board> findByTitleContaining(String title, Pageable pageable);

    Page<Board> findAllByOrderByCountDesc(Pageable pageable);

    Page<Board> findAllByOrderByViewsDesc(Pageable pageable);

    Page<Board> findAllByOrderByDateTimeDesc(Pageable pageable);

    Page<Board> findAllByOrderByBoardLikeDesc(Pageable pageable);

}
