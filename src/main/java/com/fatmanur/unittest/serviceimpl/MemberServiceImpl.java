package com.fatmanur.unittest.serviceimpl;

import com.fatmanur.unittest.model.*;
import com.fatmanur.unittest.repository.MemberRepository;
import com.fatmanur.unittest.service.AttendantService;
import com.fatmanur.unittest.service.BookService;
import com.fatmanur.unittest.service.MemberService;
import com.fatmanur.unittest.service.TranscriptItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {
    private final BookService bookService;
    private final AttendantService attendantService;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(BookService bookService, AttendantService attendantService, MemberRepository memberRepository) {
        this.bookService = bookService;
        this.attendantService = attendantService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addBook(String memberId, Book book) {
        addBook(memberId, book, new Semester());
    }

    @Override
    public void addBook(String memberId, Book book, Semester semester) {
        final Member member= member(memberId);
        member.addBook(attendantLibraryRecord(book,semester),book);
        memberRepository.save(member);
    }

    private AttendantLibraryRecord attendantLibraryRecord(Book book, Semester semester) {
        final Book bookFromRepo = book(book);
        Library attendantlibrary= bookService.findLibraryByBook(bookFromRepo);
        final Attendant attendant = attendant(attendantlibrary, semester);
        return attendant.attendantLibraryRecord(attendantlibrary, semester);
    }

    private Attendant attendant(Library library, Semester semester) {
        return attendantService.findAttendant(library, semester)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find a attendant with info<%s>", library)));
    }

    private Book book(Book book) {
        return bookService.findBook(book)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find a book with info<%s>", book)));
    }

    private Member member(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find a member with id<%s>", memberId)));
    }

    @Override
    public void dropBook(String memberId, Book book) {
        final Member member = member(memberId);
        member.dropBook(attendantLibraryRecord(book, new Semester()));
        memberRepository.save(member);
    }

    @Override
    public void addGrade(String memberId, Book book, MemberBookRecord.Grade grade) {
        final Member member = member(memberId);
        member.addGrade(attendantLibraryRecord(book, new Semester()), grade);
        memberRepository.save(member);
    }

    @Override
    public boolean isTakeBook(String memberId, Book book) {
        return false;
    }

    @Override
    public BigDecimal gpa(String memberId) {
        return member(memberId).gpa();
    }



    @Override
    public Optional<Member> findMember(String memberId) {
        return memberRepository.findById(memberId);

    }

    @Override
    public void deleteMember(String memberId) {
        final Member member = member(memberId);
        memberRepository.delete(member);
    }
}
