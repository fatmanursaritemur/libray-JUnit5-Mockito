package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.AttendantLibraryRecord;
import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class MemberWithNestedTestWithAssertJAssertions {
    @Nested
    @DisplayName("Create Member with assertjs")
    @Tag("createMember")
    class CreateMember {

        @Test
        @DisplayName("Test every member must have an id, name and surname with grouped assertions")
        @Tag("createMember")
        void shouldCreateMembertWithIdNameAndSurnameWithGroupedAssertions() {


            Member ahmet=new Member("123","ahmet","boz");
            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(ahmet.getName()).as("Ahmet's name").isEqualTo("ahmet");
            softAssertions.assertThat(ahmet.getName()).as("ahmet's name isn't").isNotEqualTo("kemal");
            softAssertions.assertAll();

            // and any failures will be reported together.
            assertAll("Student's name character check",
                    () -> assertTrue(ahmet.getName().startsWith("a")),
                    () -> assertTrue(ahmet.getName().startsWith("a"), () -> "Member's name " + "starts with a"),
                    () -> assertFalse(() -> {
                        Member ayse = new Member("id1", "Ayse", "Deve");
                        return ayse.getName().endsWith("L");
                    }, () -> "Member's name " + "ends with L")
            );

            //dependent assertions
            assertAll(() -> {
                        final Member deniz = new Member("2", "Deniz", "Yilmaz");

                        assertArrayEquals(new String[]{"ahmet", "Deniz"}, Stream.of(ahmet, deniz).map(Member::getName).toArray());
                    },
                    () -> {
                        Member anonim = ahmet;
                        final Member lale = new Member("2", "Lale", "Demir");

                        assertSame(ahmet, anonim); // ahmet == anonim
                        assertNotSame(anonim, lale);
                    });
        }


    }
    @Nested
    @DisplayName("Add Book")
    @Tag("addBook")
    class AddBookToMember {


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

            final Member member = new Member("1", "Mehmet", "Naz");
            AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(null, null);
            // assertTimeout(Duration.ofMillis(1), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god"))); 1 is too short for this process, we have a error
            assertTimeout(Duration.ofMillis(10), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god")));

            assertTimeoutPreemptively(Duration.ofMillis(10), () -> member.addBook(attendantLibraryRecord,new Book("arrow of god")));
        }

    }
}
