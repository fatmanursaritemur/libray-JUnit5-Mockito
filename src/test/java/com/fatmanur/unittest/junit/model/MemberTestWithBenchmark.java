package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.AttendantLibraryRecord;
import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTestWithBenchmark {
    @ExtendWith(BenchmarkExtension.class)
    @Nested
    @DisplayName("Add Book")
    @Tag("addBook")
    class AddBookToMember {
        @Test
        @DisplayName("Got an exception when add a null attendant book record to Member")
        @Tags({@Tag("exceptional"), @Tag("addBook")})
        void throwsExceptionWhenAddToNullCourseToMember() {

            final Member ahmet = new Member("1", "Ahmet", "Can");
            assertThrows(IllegalArgumentException.class, () -> ahmet.addBook(null,null), "Throws IllegalArgumentException");
            //    assertThrows(IllegalArgumentException.class, () -> ahmet.addBook(new AttendantLibraryRecord(null,null),null)); // no control attendantlibraryrecord service
            final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ahmet.addBook(null,null));
            assertEquals("Can't borrow book with null attendant library record", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("Add course to a student less than 10ms")
        @Tag("addCourse")
        void addBookToMemberWithATimeConstraint() {

            assertTimeout(Duration.ofMillis(10), () -> {
                //nothing will be done and this code run under 10ms
            });

            final String result = assertTimeout(Duration.ofMillis(10), () -> {
                //return a string and this code run under 10ms
                return "some string result";
            });
            assertEquals("some string result", result);

            final Member member = new Member("1", "Ahmet", "Can");
            AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(null, null);
            // assertTimeout(Duration.ofMillis(1), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god"))); 1 is too short for this process, we have a error
            assertTimeout(Duration.ofMillis(10), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god")));

            assertTimeoutPreemptively(Duration.ofMillis(10), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god")));
        }

    }
}
