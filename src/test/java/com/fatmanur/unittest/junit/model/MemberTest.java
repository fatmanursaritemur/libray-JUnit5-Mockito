package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.AttendantLibraryRecord;
import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("member simple test - 1")
public class MemberTest {
    @DisplayName("Test every member must have an id, name and surname")
    @Tag("createStudent")
    @Test
    void shouldCreateMemberWithNameAndSurname()
    {
      Member ahmet=new Member("123","ahmet","yilmaz");
      assertEquals("ahmet",ahmet.getName());
      assertEquals("123",ahmet.getId(),"Member's id");
   assertNotEquals("kara",ahmet.getSurname());
   assertTrue(ahmet.getSurname().startsWith("y"));
  assertFalse(()->{
      Member ayse=new Member("456","ayse","kara");
      return ayse.getId().contains("31");
          }, () -> "Member's id  contains 31"
  );
        final Member zeynep = new Member("2", "Zeynep", "Yıldız");

        Member student = zeynep;
        assertSame(zeynep, student); // zeynep == student
        assertNotSame(ahmet, student); // ahmet!=student
        assertArrayEquals(new String[]{"ahmet", "Zeynep"}, Stream.of(ahmet, zeynep).map(Member::getName).toArray());


    }
    @Test
    @DisplayName("Test every member must have an id, name and surname with grouped assertions")
    @Tag("createMember")
    void shouldCreateMembertWithIdNameAndSurnameWithGroupedAssertions() {


        // In a grouped assertion all assertions are executed,
        Member ahmet=new Member("123","ahmet","yilmaz");

        assertAll("Member's name check",
                () -> assertEquals("ahmet", ahmet.getName()),
                () -> assertEquals("AHMET", ahmet.getName().toUpperCase(), "Member's name"),
                () -> assertNotEquals("yahmet", ahmet.getName(), "Member's name")
        );

        // and any failures will be reported together.
        assertAll("Member's name character check",
                () -> assertTrue(ahmet.getName().startsWith("a")),
                () -> assertFalse(() -> {
                    Member mehmet = new Member("897", "Mehmet", "Can");
                    return mehmet.getName().endsWith("y");
                }, () -> "Member's name " + "ends with M")
        );

        //dependent assertions
        assertAll(() -> {
                    final Member esra = new Member("456", "Esra", "Aslan");

                    assertArrayEquals(new String[]{"Esra", "ahmet"}, Stream.of(esra, ahmet).map(Member::getName).toArray());
                },
                () -> {
                    Member student = ahmet;
                    final Member deniz = new Member("2", "Deniz", "Yilmaz");

                    assertSame(ahmet, student); // mucahit == student
                    assertNotSame(student, deniz);
                });
    }

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

    @Test
    @DisplayName("Test student creation at only development machine")
    @Tag("createMember")
    void shouldCreateMemberWithNameAndSurnameAtDevelopmentMachine() {

        assumeTrue(System.getProperty("ENV") != null, "Aborting Test: System property ENV doesn't exist!");
        assumeTrue(System.getProperty("ENV").equals("dev"), "Aborting Test: Not on a developer machine!");

        final Member ahmet = new Member("1", "Ahmet", "Can");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @Test
    @DisplayName("Test student creation at different environments")
    @Tag("createMember")
    void shouldCreateMemberWithNameAndSurnameWithSpecificEnvironment() {

        final Member ahmet = new Member("1", "Ahmet", "Can");

        final String env = System.getProperty("ENV");
        assumingThat(env != null && env.equals("dev"), () -> {
            AttendantLibraryRecord lecturerCourseRecord = new AttendantLibraryRecord(null, null);
            ahmet.addBook(lecturerCourseRecord,new Book("nana"));
            assertEquals(1, ahmet.getMemberBookRecords().size());
        });

        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @Disabled("No more valid scenario")
    @Test
    @DisplayName("Test that member must have only number id")
    @Tag("createMember")
    void shouldCreateMemberWithNumberId() {
        assertThrows(IllegalArgumentException.class, () -> new Member("id", "Ahmet", "Can"));
    }
}

