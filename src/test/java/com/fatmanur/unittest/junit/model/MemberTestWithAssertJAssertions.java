package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class MemberTestWithAssertJAssertions {

    @Test
    void CreateMember() {
        final Member expMember = new Member("first", "Nur", "Kara");
        Assertions.assertThat(expMember.getName()).as("Member name is %s",expMember.getName())
                .doesNotContainOnlyWhitespaces()
                .isNotBlank()
                .isNotEmpty()
                .isEqualTo("Nur")
                .isEqualToIgnoringCase("NUR")
                .isEqualToIgnoringNewLines("Nur")
                .isIn("Nur","Ali")
                .startsWith("N")
                .endsWith("r")
                .doesNotEndWith("a")
                .contains("ur")
                .contains(List.of("ur","Nu"))
                .hasSize(3)
               .matches("^N\\w{1}r$") ;
    }
    @Test
    void addBookToMember() {

        final Member ahmet = new Member("id1", "Ahmet", "Kara", LocalDate.of(1990, 1, 1));
        final Member mehmet = new Member("id2", "Mehmet", "Kural", LocalDate.of(1992, 1, 1));
        final Member canan = new Member("id3", "Canan", "Sahin", LocalDate.of(1995, 1, 1));
        final Member elif = new Member("id4", "Elif", "Oz", LocalDate.of(1991, 1, 1));
        final Member hasan = new Member("id5", "Hasan", "Kartal", LocalDate.of(1990, 1, 1));
        final Member mucahit = new Member("id6", "Mucahit", "Kurt", LocalDate.of(1980, 1, 1));

        final List<Member> students = List.of(ahmet, mehmet, canan, elif, hasan);
        Assertions.assertThat(students)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(ahmet, mehmet)
                .contains(ahmet, Index.atIndex(0))
                .containsOnly(ahmet, mehmet, canan, hasan, elif)
                .containsExactly(ahmet, mehmet, canan, elif, hasan)
                .containsExactlyInAnyOrder(ahmet, mehmet, canan, hasan, elif);

        Assertions.assertThat(students)
                .filteredOn(student -> student.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS)>=27)
                .hasSize(4)
                . containsOnly(ahmet, mehmet, elif, hasan);


        Assertions.assertThat(students)
                .filteredOn(new Condition<>() {
                    @Override
                    public boolean matches(Member value) {
                        return value.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= 27;
                    }
                })
                .containsOnly(ahmet, mehmet, elif, hasan)
        ;

        Assertions.     assertThat(students)
                .filteredOn("birthDate", in(LocalDate.of(1990, 1, 1)))
                .hasSize(2)
                .containsOnly(ahmet, hasan)
        ;

        assertThat(students).as("Member's List")
                .extracting(Member::getName)
                .filteredOn(name -> name.contains("t"))
                .hasSize(2)
                .containsOnly("Ahmet", "Mehmet")
        ;

        assertThat(students)
                .filteredOn(student -> student.getName().contains("t"))
                .extracting(Member::getName, Member::getSurname)
                .containsOnly(
                        tuple("Ahmet", "Kara"),
                        tuple("Mehmet", "Kural")
                )
        ;
        final AttendantLibraryRecord attendantLibraryRecord1 = new AttendantLibraryRecord(new Library("1"), new Semester());
        final AttendantLibraryRecord attendantLibraryRecord2 = new AttendantLibraryRecord(new Library("2"), new Semester());
        final AttendantLibraryRecord attendantLibraryRecord3 = new AttendantLibraryRecord(new Library("3"), new Semester());

        ahmet.addBook(attendantLibraryRecord1, new Book("1"));
        ahmet.addBook(attendantLibraryRecord2,new Book("2"));

        canan.addBook(attendantLibraryRecord1, new Book("1"));
        canan.addBook(attendantLibraryRecord2,new Book("2"));
        canan.addBook(attendantLibraryRecord3,new Book("3"));
        assertThat(students)
                .filteredOn("name", in("Ahmet", "Canan"))
                .flatExtracting(student -> student.getMemberBookRecords())
                .hasSize(5)
                .filteredOn(studentBookRecord -> studentBookRecord.getAttendantLibraryRecord().getLibrary().getCode().equals("1"))
                .hasSize(2)
        ;

    }
    @Test
    void anotherCreateMember()
    {
        final Member ahmet = new Member("id1", "Ahmet", "Yilmaz");
        final Member mehmet = new Member("id2", "Mehmet", "Yilmaz");

        Assertions.assertThat(ahmet).as("Checking things about ahmet")
                .isNotNull()
                .isExactlyInstanceOf(Member.class)
                .isInstanceOf(UniversityMember.class)
                .hasSameClassAs(mehmet)
                .isNotEqualTo(mehmet)
                .isEqualToComparingOnlyGivenFields(mehmet,"surname")
                .isEqualToIgnoringGivenFields(mehmet,"id","name","birthDate");
        MemberAssert.assertThat(mehmet)
                .hasSurName("Yilmaz");
    }
    @Test
    void addBookToMemberWithExceptionScenario()
    {
        final Member ahmet = new Member("id1", "Ahmet", "Yilmaz");
        Assertions.assertThatThrownBy(()->ahmet.addBook(null,null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't borrow book ")
                .hasStackTraceContaining("Member");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->ahmet.addBook(null,null))
                .withMessageContaining("Can't borrow book")
                .withNoCause();

        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(()->ahmet.addBook(null,null))
                .withNoCause();

    }

}
