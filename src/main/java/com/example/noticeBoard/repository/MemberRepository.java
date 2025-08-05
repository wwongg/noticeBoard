package com.example.noticeBoard.repository;

import com.example.noticeBoard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // JpaRepository<Member, Long> 를 상속하여 Spring Data JPA가 자동으로 구현체를 만들고
    // save()를 비롯한 여러 CRUD 메서드를 사용할 수 있게 한다.
    Member findByUsername(String username);


}
