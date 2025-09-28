package com.example.noticeBoard.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_Id")
    private Long id;

    private String username;
    private String email;
    private String password;





        @JsonManagedReference
        @OneToMany(mappedBy = "member")
        private List<Board> boards = new ArrayList<>();


}
