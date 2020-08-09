package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberTestWithTemplate {
    private Member member;

    @BeforeAll
    void setUp() {
        member = new Member("123","Ayse","Kara");
    }

    @DisplayName("Add Book to Member")
    @TestTemplate
    @ExtendWith(RepeatedStudentTestTemplateInvocationContextProvider.class)
    void addBookToMember(AttendantLibraryRecord attendantLibraryRecord, int numberOfCall) {
        member.addBook(attendantLibraryRecord,new Book());
        assertEquals(numberOfCall, member.getMemberBookRecords().size());
    }
}
