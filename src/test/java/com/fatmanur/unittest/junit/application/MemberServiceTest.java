package com.fatmanur.unittest.junit.application;

import com.fatmanur.unittest.model.*;
import com.fatmanur.unittest.repository.MemberRepository;
import com.fatmanur.unittest.service.AttendantService;
import com.fatmanur.unittest.service.BookService;
import com.fatmanur.unittest.service.MemberService;
import com.fatmanur.unittest.serviceimpl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @Test
    void  addBook()
    {
        BookService mockBookS = mock(BookService.class);
        AttendantService mockAttendantS = mock(AttendantService.class);
        MemberRepository mockMemberR = mock(MemberRepository.class);
        Library mockLibrary=mock(Library.class);

        Library library = new Library("111");
        Book book = new Book("121");
        when(mockBookS.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Semester mockSemester = mock(Semester.class);
        Attendant mockAttendant = mock(Attendant.class);
        Member fMember = new Member("f12", "ahmet", "kara");
        when(mockBookS.findLibraryByBook(book)).thenReturn(library);
        when(mockAttendantS.findAttendant(mockLibrary,semester)).thenReturn(Optional.of(mockAttendant));
        when(mockMemberR.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(mockAttendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(mockAttendantS.findAttendant(library, semester)).thenReturn(Optional.of(mockAttendant));
        final MemberServiceImpl memberService=new MemberServiceImpl(mockBookS,mockAttendantS,mockMemberR);

        memberService.addBook("f12",book,semester);
        assertThatThrownBy(() -> memberService.findMember("f12")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Member> student = memberService.findMember("f12");
        assertThat(student).as("expect Member")
                .isPresent()
                .get()
                .matches(student1 -> student1.isTakeBook(book));
        verify(mockBookS).findBook(book);
        verify(mockBookS, times(1)).findBook(book);
        verify(mockBookS, atLeast(1)).findBook(book);
        verify(mockBookS, atMost(1)).findBook(book);
        verify(mockMemberR, times(3)).findById("f12"); //because we call 2 theReturn
        // and one thenThrow in 23. line
      verify(mockAttendantS).findAttendant(any(Library.class), any(Semester.class));
       verify(mockAttendant).attendantLibraryRecord(argThat(argument -> argument.getCode().equals("111")), any(Semester.class));
       verify(mockAttendant).attendantLibraryRecord(argThat(new MyLibraryArgumentMatcher()), any(Semester.class));


    }

    @Test
    void dropBook() {

        BookService mockBookS = mock(BookService.class);
        AttendantService mockAttendantS = mock(AttendantService.class);
        MemberRepository mockMemberR = mock(MemberRepository.class);
        Library mockLibrary=mock(Library.class);

        Library library = new Library("111");
        Book book = new Book("121");
        when(mockBookS.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Semester mockSemester = mock(Semester.class);
        Attendant mockAttendant = mock(Attendant.class);
        Member fMember = new Member("f12", "ahmet", "kara");
        when(mockBookS.findLibraryByBook(book)).thenReturn(library);
        when(mockAttendantS.findAttendant(mockLibrary,semester)).thenReturn(Optional.of(mockAttendant));
        when(mockMemberR.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(mockAttendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(mockAttendantS.findAttendant(library, semester)).thenReturn(Optional.of(mockAttendant));
        final MemberServiceImpl studentService=new MemberServiceImpl(mockBookS,mockAttendantS,mockMemberR);

        when(mockBookS.findBook(book))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(book));
        final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(library, new Semester());
        when(mockAttendant.attendantLibraryRecord(eq(library), any(Semester.class))).thenReturn(attendantLibraryRecord);
        when(mockAttendantS.findAttendant(eq(library), any(Semester.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(mockAttendant));
        final Member student = mock(Member.class);
        when(mockMemberR.findById("f12"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(student));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropBook("f12", book))
                .withMessageContaining("Can't find a member");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropBook("f12", book))
                .withMessageContaining("Can't find a book");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropBook("f12", book))
                .withMessageContaining("Can't find a attendant");

        studentService.dropBook("f12", book);


    }
    class MyLibraryArgumentMatcher implements ArgumentMatcher<Library> {
        @Override
        public boolean matches(Library library) {
            return library.getCode().equals("111");
        }
    }
}
