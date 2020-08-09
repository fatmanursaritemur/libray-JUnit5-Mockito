package com.fatmanur.unittest.model;

import com.fatmanur.unittest.exception.NoBookFoundForStudentException;
import com.fatmanur.unittest.exception.NotActiveSemesterException;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
@Data
public class Member implements UniversityMember {

    private final String id;
    private final String name;
    private final String surname;
    private LocalDate birthDate;
    private Set<MemberBookRecord> memberBookRecords = new HashSet<>();
    private Library library;

    public Member(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Member(String id, String name, String surname, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    public Member(String id, String name, String surname, Set<MemberBookRecord> memberBookRecords) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.memberBookRecords.addAll(memberBookRecords);
    }

    public void addBook(AttendantLibraryRecord attendantLibraryRecord, Book book) {

        if (attendantLibraryRecord == null) {
            throw new IllegalArgumentException("Can't borrow book with null attendant library record");
        }

        final MemberBookRecord memberBookRecord = new MemberBookRecord(attendantLibraryRecord,book);
        memberBookRecords.add(memberBookRecord);
    }

    public void dropBook(AttendantLibraryRecord attendantLibraryRecord) {

        if (attendantLibraryRecord == null) {
            throw new IllegalArgumentException("Can't drop course with null lecturer course record");
        }

        final MemberBookRecord MemberCourseRecordWillBeDropped = memberBookRecords.stream()
                .filter(memberBookRecord -> memberBookRecord.getAttendantLibraryRecord().equals(attendantLibraryRecord))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("There is no Member course record for given lecturer course record"));

        if (!MemberCourseRecordWillBeDropped.getAttendantLibraryRecord().getSemester().isAddDropAllowed()) {
            throw new NotActiveSemesterException("Add drop period is closed for the semester");
        }

        memberBookRecords.remove(MemberCourseRecordWillBeDropped);
    }


    public boolean isTakeBook(Book book) { //buna baaaaaak
        return memberBookRecords.stream()
                .map(MemberBookRecord::getBook)
                .anyMatch(book1 -> book1.equals(book));
    }

    public void addGrade(AttendantLibraryRecord attendantLibraryRecord, MemberBookRecord.Grade grade) {

        final MemberBookRecord MemberBookRecord1 = memberBookRecords.stream()
                .filter(MemberBookRecord -> MemberBookRecord.getAttendantLibraryRecord().equals(attendantLibraryRecord))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                new NoBookFoundForStudentException(String.format("Member didn't take any book for attendant library record: %s", attendantLibraryRecord == null ? "null" : attendantLibraryRecord.getLibrary().getCode()))

                        )

                );
        MemberBookRecord1.setUsegeScore(grade);
    }

    public BigDecimal gpa() {

        int totalCredit = 0;
        BigDecimal weightedGpa = BigDecimal.ZERO;

        for (MemberBookRecord memberBookRecord : memberBookRecords) {
            totalCredit += bookCredit(memberBookRecord);
            weightedGpa = weightedGpa.add(BigDecimal.valueOf(bookCredit(memberBookRecord)).multiply(courseGrade(memberBookRecord)));
        }

        return weightedGpa.divide(BigDecimal.valueOf(totalCredit), 2, RoundingMode.HALF_UP);
    }

    private int bookCredit(MemberBookRecord memberBookRecord) { //bunu düzelt
        return memberBookRecord.getAttendantLibraryRecord().getOperationTime();
    }

    private BigDecimal courseGrade(MemberBookRecord memberBookRecord) {
        return memberBookRecord.getUsegeScore().getGradeInNumber();
    }


}
