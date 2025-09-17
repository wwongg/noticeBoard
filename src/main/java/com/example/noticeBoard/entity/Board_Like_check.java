package com.example.noticeBoard.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board_Like_check {

    @Id@GeneratedValue
    @Column(name = "board_like_Id")
    private long id;

    @Column(name = "board_like", nullable = false)
    private Integer boardLike = 0;

    private boolean like_check;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_Id")
    private Board board;

    @OneToOne()
    @JoinColumn(name = "member_Id")
    private Member member;

}
