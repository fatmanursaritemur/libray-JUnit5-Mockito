package com.fatmanur.unittest.service;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Member;
import com.fatmanur.unittest.model.MemberBookRecord;
import com.fatmanur.unittest.model.Semester;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MemberService {

    void addBook(String memberId, Book book);

    void addBook(String memberId, Book book, Semester semester);

    void dropBook(String memberId, Book book);

    void addGrade(String memberId, Book book, MemberBookRecord.Grade grade);

    boolean isTakeBook(String memberId, Book book);

    BigDecimal gpa(String memberId);


    Optional<Member> findMember(String memberId);

    void deleteMember(String memberId);
}
