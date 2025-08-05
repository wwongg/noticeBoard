package com.example.noticeBoard.event;

import com.example.noticeBoard.entity.Board;
import org.springframework.context.ApplicationEvent;

public class ViewsEvent extends ApplicationEvent {


    private Board board;

    // 생성자
    public ViewsEvent(Board board) {
        super(board);
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
