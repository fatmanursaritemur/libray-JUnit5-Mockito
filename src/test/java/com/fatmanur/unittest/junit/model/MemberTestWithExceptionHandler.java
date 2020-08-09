package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.AttendantLibraryRecord;
import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberTestWithExceptionHandler {
    @ExtendWith(IllegalArgumentExceptionHandlerExtension.class)
    @Nested
    @DisplayName("Add Course to Student(Exceptional)")
    @Tag("exceptional")
    class AddBookToMember {
        @Test
        @DisplayName("Got an exception when add a null attendant library  record to member")
        void throwsExceptionWhenAddToNullCourseToMember() {

            final Member ahmet = new Member("1", "Ahmet", "Can");
            ahmet.addBook(null,null);
        }
    }
}
