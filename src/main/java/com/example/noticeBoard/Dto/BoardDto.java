package com.example.noticeBoard.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private LocalDateTime dateTime;

    private Integer board_like;

    private String writer;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private MemberDto memberDto;

    private BoardLikeDto boardLikeDto;
}
