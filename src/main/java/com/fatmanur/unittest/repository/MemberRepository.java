package com.fatmanur.unittest.repository;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository  {

    Optional<Member> findById(String id);

    Member save(Member member);

    void delete(Member member);
}
