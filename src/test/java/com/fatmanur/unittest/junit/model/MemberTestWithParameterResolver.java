package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.exception.NotActiveSemesterException;
import com.fatmanur.unittest.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class MemberTestWithParameterResolver {
    @Nested
    @ExtendWith(DropBookingParameterResolver.class)
    @DisplayName("Drop Booking")
    @Tag("dropBooking")
    class DropBook {

        final Member nur;
        final Semester addDropPeriodOpenSemester;
        final Semester addDropPeriodClosedSemester;
        final Semester notActiveSemester;

        DropBook(Member nur, Semester addDropPeriodOpenSemester, Semester addDropPeriodClosedSemester, Semester notActiveSemester) {
            this.nur = nur;
            this.addDropPeriodOpenSemester = addDropPeriodOpenSemester;
            this.addDropPeriodClosedSemester = addDropPeriodClosedSemester;
            this.notActiveSemester = notActiveSemester;
        }


        @TestFactory
        Stream<DynamicTest> dropbookingfromember() {
            return Stream.of(DynamicTest.dynamicTest("throws illegal argument exception for null attendant library record"
                    , () -> {
                        assertThrows(IllegalArgumentException.class, () -> nur.dropBook(null));
                    }),
                    DynamicTest.dynamicTest("throws illegal argument exception if the member did't register book before"
                            , () -> {
                                final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
                                assertThrows(IllegalArgumentException.class, () -> nur.dropBook(attendantLibraryRecord));
                            }),
                    DynamicTest.dynamicTest("throws not active semester exception if the semester is not active"
                            , () -> {
                                final Semester nonactivesemester = notActiveSemester;
                                assumeTrue(!nonactivesemester.isActive());
                                final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), nonactivesemester);
                                final Member zeynep = new Member("23", "zeynep", "kaya", Set.of(new MemberBookRecord(attendantLibraryRecord, new Book())));
                                assertThrows(NotActiveSemesterException.class, () -> zeynep.dropBook(attendantLibraryRecord));
                            }),
                    DynamicTest.dynamicTest("throws not active semester exception if the add drop period is closed for the semester"
                            , () -> {
                                final Semester addDropPeriodClosed = addDropPeriodClosedSemester;
                                assumeTrue(!addDropPeriodClosed.isAddDropAllowed());
                                final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), addDropPeriodClosed);
                                final Member stundentzeynep = new Member("23", "zeynep", "kaya", Set.of(new MemberBookRecord(attendantLibraryRecord, new Book())));
                                assertThrows(NotActiveSemesterException.class, () -> stundentzeynep.dropBook(attendantLibraryRecord));
                            }),
                    DynamicTest.dynamicTest("drop book from member"
                            , () -> {
                                final Semester addDropPeriodOpenS = addDropPeriodOpenSemester;
                                assumeTrue(addDropPeriodOpenS.isAddDropAllowed());
                                final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), addDropPeriodOpenS);
                                Member mehmet = new Member("id1", "Mehmet", "Yilmaz", Set.of(new MemberBookRecord(attendantLibraryRecord, new Book())));
                                assertEquals(1, mehmet.getMemberBookRecords().size());
                                mehmet.dropBook(attendantLibraryRecord);
                                assertTrue(mehmet.getMemberBookRecords().isEmpty());
                            })
            );
        }

    }
}
