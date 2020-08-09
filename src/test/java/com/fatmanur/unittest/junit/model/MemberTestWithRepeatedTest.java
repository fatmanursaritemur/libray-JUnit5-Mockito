package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberTestWithRepeatedTest implements TestLifecycleReporter{
        private Member member;

        @BeforeAll
        void setUp() {
            member = new Member("123","Ayse","Kara");
        }

        @DisplayName("Add Book to Member")
        @RepeatedTest(value = 8, name = "{displayName} => Add one Book to member and member has {currentRepetition} Books")
        void addBookToMember(RepetitionInfo repetitionInfo) {

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(String.valueOf(repetitionInfo.getCurrentRepetition())), new Semester());
            member.addBook(attendantLibraryRecord,new Book());
            assertEquals(repetitionInfo.getCurrentRepetition(), member.getMemberBookRecords().size());
        }
}