package com.example.noticeBoard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity // jpa가 해당 클래스를 엔티티로 인식할 수 있게 함.
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_Id")
    private Long Id;

    private String title;

    private String writer;
    private String password;

    private LocalDateTime dateTime;
    private String content;

    private Integer views;

    private Integer count;

    private Integer boardLike;

        // json 직렬화가 무한루프에 빠지고 있어 부모 쪽에 @JsonManagedReference
        // Board -> Member 참조는 직렬화 과정에서 무시되어 무한루프 방지된다.
        @JsonBackReference
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_Id")
        private Member member;

        // 변경 작업을 수행할 때, 엔티티에서 값을 가져와서 업데이트하는 방식
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
