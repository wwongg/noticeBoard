package com.example.noticeBoard.repository;


import com.example.noticeBoard.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    public Optional<Email> findByEmail(String email);

}
