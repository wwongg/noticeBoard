package com.example.noticeBoard.listner;

import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.event.ViewsEvent;
import com.example.noticeBoard.service.Impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickViewListener implements ApplicationListener<ViewsEvent> {

    private final BoardServiceImpl boardService;

    // ViewsEvent가 발생할 떄 자돟으로 호출되는 메서드.
    @Override
    public void onApplicationEvent(ViewsEvent event) {
        Board board = event.getBoard();
        boardService.countViews(board.getId());
    }
}
