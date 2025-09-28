package com.example.noticeBoard.service.Impl;


import com.example.noticeBoard.Dto.BoardLikeDto;
import com.example.noticeBoard.entity.Board;
import com.example.noticeBoard.entity.Board_Like_check;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.BoardLikeRepository;
import com.example.noticeBoard.service.BoardLikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeServiceImpl implements BoardLikeService {

    private final MemberServiceImpl memberService;
    private final BoardLikeRepository boardLikeRepository;

    public boolean isBoardCheck(Member loginMember, Long boardId){
        String loginMemberUsername = loginMember.getUsername();
        Member member = memberService.findByUsername(loginMemberUsername);

        Board_Like_check boardLike = boardLikeRepository.findMemberId(member.getId(), boardId);

        if(boardLike != null && boardLike.isLike_check()) {
            return true;
        }

        return false;
    }


    @Override
    public void deleteByBoardId(Long boardId) {
        boardLikeRepository.deleteBoard_Id(boardId);
    }

    @Override
    public List<Board> findLikedBoards(Member member) {
        return boardLikeRepository.findByMember(member) // List<Board_Like_check>
                .stream()
                .filter(Board_Like_check::isLike_check) // 엔티티 메서드 호출
                .map(Board_Like_check::getBoard) // 엔티티의 getBoard() 호출
                .collect(Collectors.toList());
    }


}
