package com.example.noticeBoard.service.Impl;


import com.example.noticeBoard.entity.Board_Like_check;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.repository.BoardLikeRepository;
import com.example.noticeBoard.service.BoardLikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
