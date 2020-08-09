package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MemberTestWithDynamicTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("id1", "Ayse", "Kara");
    }

    @TestFactory
    Stream<DynamicNode> addBookToMemberWithBookCodeAndBookType(TestReporter testReporter) {

        return Stream.of("101", "103", "105")
                .map(bookCode -> dynamicContainer("Add book<" + bookCode + "> to member",
                        Stream.of(Book.BookType.HistoricalFiction, Book.BookType.Classic,Book.BookType.Poetry,Book.BookType.Horror, Book.BookType.SciFi)
                                .map(bookType -> dynamicTest("Add book<" + bookType + "> to member", () -> {
                                    final Book book = Book.newBook().withCode(bookCode).withBookType(bookType).book();
                                    final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
                                    member.addBook(attendantLibraryRecord,book);
                                    assertTrue(member.isTakeBook(book));
                                    testReporter.publishEntry("Member Book Size", String.valueOf(member.getMemberBookRecords().size()));
                                }))
                ));

    }

    @TestFactory
    Stream<DynamicTest> addBookToMemberWithBookCode() {
        final Iterator<String> bookCodeGenerator = Stream.of("101", "103", "105").iterator();
        Function<String, String> displayNameGenerator = bookCode -> "Add book<" + bookCode + "> to member";
        ThrowingConsumer<String> testExecutor = bookCode -> {
            final Book book = Book.newBook().withCode(bookCode).withBookType(Book.BookType.HistoricalFiction).book();
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertTrue(member.isTakeBook(book));

        };


        return DynamicTest.stream(bookCodeGenerator, displayNameGenerator, testExecutor);
    }
}
