package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberTestWithParameterizedMethods {

    private Member member;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ValueSourceDemo {

        private int memberbookSize = 0;

        @BeforeAll
        void setUp() {
            member = new Member("123","Ayse","Kara");
        }

        @ParameterizedTest
        @ValueSource(strings = {"101", "103", "105"})
        void addbookToMember(String libraryCode) {

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(libraryCode), new Semester());
            member.addBook(attendantLibraryRecord,new Book(libraryCode));
            memberbookSize++;
            assertEquals(memberbookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(libraryCode)));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class EnumSourceDemo {

        @BeforeAll
        void setUp() {
            member = new Member("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @EnumSource(Book.BookType.class)
        void addbookMember(Book.BookType bookType) {

            final Book book = Book.newBook()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withBookType(bookType).book();

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
        }

        @ParameterizedTest
        @EnumSource(value = Book.BookType.class, names = "HistoricalFiction")
        void addHistoricalFictionbookToMember(Book.BookType bookType) {
            final Book book = Book.newBook()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withBookType(bookType).book();

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
            assertEquals(Book.BookType.HistoricalFiction, book.getBookType());
        }

        @ParameterizedTest
        @EnumSource(value = Book.BookType.class, mode = EnumSource.Mode.EXCLUDE, names = "HistoricalFiction")
        void addNotHistoricalFictionBookToMember(Book.BookType bookType) {
            final Book book = Book.newBook()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withBookType(bookType).book();

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
            assertNotEquals(Book.BookType.HistoricalFiction, book.getBookType());
        }


    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class MethodSourceDemo {
        private int memberBookSize = 0;

        @BeforeAll
        void setUp() {
            member = new Member("123","Ayse","Kara");
        }

        @ParameterizedTest
        @MethodSource
        void addBookToMember(String libraryCode) {
           
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(libraryCode), new Semester());
            member.addBook(attendantLibraryRecord,new Book(libraryCode));
            memberBookSize++;
            assertEquals(memberBookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(libraryCode)));
        }

        Stream<String> addBookToMember() {
            return Stream.of("101", "103", "105");
        }

        @ParameterizedTest
        @MethodSource("courseWithCodeAndType")
        void addBookToMember(String bookCode, Book.BookType bookType) {

            final Book book = new Book(bookCode);
            book.setBookType(bookType);
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            memberBookSize++;
            assertEquals(memberBookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(bookCode)));
            assumingThat(bookCode.equals("10"),
                    () -> assertEquals(Book.BookType.HistoricalFiction, bookType)
            );
            assumingThat(bookCode.equals("11"),
                    () -> assertEquals(Book.BookType.Classic, bookType)
            );
            assumingThat(bookCode.equals("12"),
                    () -> assertEquals(Book.BookType.Poetry, bookType)
            );
        }

        Stream<Arguments> courseWithCodeAndType() {
            return Stream.of(
                    Arguments.of("10", Book.BookType.HistoricalFiction),
                    Arguments.of("11", Book.BookType.Classic),
                    Arguments.of("12", Book.BookType.Poetry)
            );
        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class CsvSourceDemo {

        private int memberBookSize = 0;

        @BeforeAll
        void setUp() {
            member = new Member("id1", "Ahmet", "Yilmaz");
        }

        @DisplayName("Add Book to Member")
        @ParameterizedTest(name = "{index} ==> Parameters: first:{0}, second:{1}")
        @CsvSource({"11,HistoricalFiction", "12, Classic", "13, Poetry"})
        void addBookToMember(String bookCode, Book.BookType bookType) {

            final Book book = new Book(bookCode);
            book.setBookType(bookType);
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            memberBookSize++;
            assertEquals(memberBookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(bookCode)));
            assumingThat(bookCode.equals("11"),
                    () -> assertEquals(Book.BookType.HistoricalFiction, bookType)
            );
            assumingThat(bookCode.equals("12"),
                    () -> assertEquals(Book.BookType.Classic, bookType)
            );
            assumingThat(bookCode.equals("13"),
                    () -> assertEquals(Book.BookType.Poetry, bookType)
            );
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/bookCodeAndTypes.csv", numLinesToSkip = 1)
        void addBookToMemberWithCsvFile(String bookCode, Book.BookType bookType) {

            final Book book = new Book(bookCode);
            book.setBookType(bookType);
            final AttendantLibraryRecord lecturerBookRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(lecturerBookRecord, book);
            memberBookSize++;
            assertEquals(memberBookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(bookCode)));
            assumingThat(bookCode.equals("11"),
                    () -> assertEquals(Book.BookType.HistoricalFiction, bookType)
            );
            assumingThat(bookCode.equals("12"),
                    () -> assertEquals(Book.BookType.Classic, bookType)
            );
            assumingThat(bookCode.equals("13"),
                    () -> assertEquals(Book.BookType.Poetry, bookType)
            );
        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ArgumentsSourceDemo {
        private int memberBookSize = 0;

        @BeforeAll
        void setUp() {
            member = new Member("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @ArgumentsSource(BookCodeAndTypeProvider.class)
        void addBookToMember(String bookCode, Book.BookType bookType) {

            final Book book = new Book(bookCode);
            book.setBookType(bookType);
            final AttendantLibraryRecord lecturerBookRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(lecturerBookRecord, book);
            memberBookSize++;
            assertEquals(memberBookSize, member.getMemberBookRecords().size());
            assertTrue(member.isTakeBook(new Book(bookCode)));
            assumingThat(bookCode.equals("10"),
                    () -> assertEquals(Book.BookType.HistoricalFiction, bookType)
            );
            assumingThat(bookCode.equals("11"),
                    () -> assertEquals(Book.BookType.Classic, bookType)
            );
            assumingThat(bookCode.equals("12"),
                    () -> assertEquals(Book.BookType.Poetry, bookType)
            );
        }
    }

    static class BookCodeAndTypeProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("10", Book.BookType.HistoricalFiction),
                    Arguments.of("11", Book.BookType.Classic),
                    Arguments.of("12", Book.BookType.Poetry)

            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TypeConversionAndCustomDisplayNameDemo {
        // enum conversion

        @BeforeAll
        void setUp() {
            member = new Member("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @ValueSource(strings = {"HistoricalFiction", "Classic","Poetry"})
        void addBookMember(Book.BookType bookType) {

            final Book book = Book.newBook()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withBookType(bookType).book();

            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
        }

        // user guide for other options


        // factory method or constructor conversion
        @ParameterizedTest
        @ValueSource(strings = {"101", "103"})
        void addBookMember(Book book) {
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
        }

        // conversion using SimpleConverter with1 @ConvertWith
        @ParameterizedTest
        @ValueSource(strings = {"101", "103", "105"})
        void addBookMemberWithConverter(@ConvertWith(BookConverter.class) Book book) {
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester());
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
        }


        // conversion with @JavaTimeConversionPattern
        @DisplayName("Add course with localdate info")
        @ParameterizedTest(name = "{index} ==> Parametreler: {arguments}")
        @ValueSource(strings = {"01.09.2018", "01.01.2018", "01.06.2018"})
        void addBookMemberWithLocalDate(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate localDate) {
            final Book book = Book.newBook().withCode(String.valueOf(new Random().nextInt(100))).book();
            final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(new Library(), new Semester(localDate));
            member.addBook(attendantLibraryRecord,book);
            assertFalse(member.getMemberBookRecords().isEmpty());
            assertTrue(member.isTakeBook(book));
        }


        //display name {index}, {arguments}, {0} usage
    }

    static class BookConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            return new Book(((String) source));
        }
    }

}
