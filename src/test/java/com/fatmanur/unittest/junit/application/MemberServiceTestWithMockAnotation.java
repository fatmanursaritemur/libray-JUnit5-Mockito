package com.fatmanur.unittest.junit.application;

import com.fatmanur.unittest.model.*;
import com.fatmanur.unittest.repository.MemberRepository;
import com.fatmanur.unittest.service.AttendantService;
import com.fatmanur.unittest.service.BookService;

import com.fatmanur.unittest.serviceimpl.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTestWithMockAnotation {

    @Mock
    private BookService bookService;

    @Mock
    private AttendantService attendantService;

    @Mock
    private Attendant attendant;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Captor
    private ArgumentCaptor<Member> memberArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    void  addBook()
    {
        Library library = new Library("111");
        Book book = new Book("121");
        when(bookService.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Member fMember = new Member("f12", "ahmet", "kara");
        when(bookService.findLibraryByBook(book)).thenReturn(library);
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(attendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(attendantService.findAttendant(library, semester)).thenReturn(Optional.of(attendant));

        memberService.addBook("f12",book,semester);
        assertThatThrownBy(() -> memberService.findMember("f12")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Member> member = memberService.findMember("f12");
        assertThat(member).as("expect Member")
                .isPresent()
                .get()
                .matches(member1 -> member1.isTakeBook(book));
        verify(bookService).findBook(book);
        verify(bookService, times(1)).findBook(book);
        verify(bookService, atLeast(1)).findBook(book);
        verify(bookService, atMost(1)).findBook(book);
        verify(memberRepository, times(3)).findById("f12"); //because we call 2 theReturn
        // and one thenThrow in 23. line
        verify(attendantService).findAttendant(any(Library.class), any(Semester.class));
        verify(attendant).attendantLibraryRecord(argThat(argument -> argument.getCode().equals("111")), any(Semester.class));
        verify(attendant).attendantLibraryRecord(argThat(new MyLibraryArgumentMatcher()), any(Semester.class));


    }

    @Test
    void dropBook() {

        Library library = new Library("111");
        Book book = new Book("121");
        when(bookService.findBook(book)).thenReturn(Optional.of(book));
        Attendant mockAttendant = mock(Attendant.class);
        Member fMember = new Member("f12", "ahmet", "kara");
        when(bookService.findLibraryByBook(book)).thenReturn(library);
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(bookService.findBook(book))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(book));
        final AttendantLibraryRecord attendantLibraryRecord = new AttendantLibraryRecord(library, new Semester());
        when(mockAttendant.attendantLibraryRecord(eq(library), any(Semester.class))).thenReturn(attendantLibraryRecord);
        when(attendantService.findAttendant(eq(library), any(Semester.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(mockAttendant));
        final Member member = mock(Member.class);
        when(memberRepository.findById("f12"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(member));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.dropBook("f12", book))
                .withMessageContaining("Can't find a member");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.dropBook("f12", book))
                .withMessageContaining("Can't find a book");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.dropBook("f12", book))
                .withMessageContaining("Can't find a attendant");

        memberService.dropBook("f12", book);


    }
    class MyLibraryArgumentMatcher implements ArgumentMatcher<Library> {
        @Override
        public boolean matches(Library library) {
            return library.getCode().equals("111");
        }
    }
    @Test
    void deleteMember() {

        final Member member = new Member("id1", "Ahmet", "Yilmaz");
        when(memberRepository.findById("id1")).thenAnswer(invocation -> Optional.of(member));
        doNothing()
                .doThrow(new IllegalArgumentException("There is no member in repo"))
                .doAnswer(invocation -> {
                    final Member memberAhmet = invocation.getArgument(0);
                    System.out.println(String.format("Member<%s> will be deleted!", memberAhmet));
                    return null;
                })
                .when(memberRepository).delete(member);
        memberService.deleteMember("id1");
        assertThatIllegalArgumentException().isThrownBy(() -> memberService.deleteMember("id1"))
                .withMessageContaining("no member");
        memberService.deleteMember("id1");
        verify(memberRepository, times(3)).findById(stringArgumentCaptor.capture());
        verify(memberRepository, times(3)).delete(memberArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getAllValues())
                .hasSize(3)
                .containsOnly("id1", "id1", "id1");

        assertThat(memberArgumentCaptor.getAllValues())
                .hasSize(3)
                .extracting(Member::getId)
                .containsOnly("id1", "id1", "id1");
    }

    @Test
    void addBookWithSpyMember() {
        Library library = new Library("111");
        Book book = new Book("121");
        when(bookService.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Member fMember = new Member("f12", "ahmet", "kara");
        when(bookService.findLibraryByBook(book)).thenReturn(library);
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(attendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(attendantService.findAttendant(library, semester)).thenReturn(Optional.of(attendant));

        final Member memberReal = new Member("id1", "Ahmet", "Yilmaz");
        final Member memberAhmet = spy(memberReal);

//        doThrow(new IllegalArgumentException("Spy failed!")).when(memberAhmet).addBook(any(LecturerBookRecord.class));


        when(memberRepository.findById(anyString()))
                .thenReturn(Optional.of(memberAhmet)).
                thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(memberAhmet));

        memberService.addBook("id1", book, semester);

        assertThat(memberAhmet)
                .matches(member -> member.isTakeBook(book));

        assertThat(memberReal)
                .matches(member -> member.isTakeBook(book));

        memberAhmet.setBirthDate(LocalDate.of(2000, 6, 17));
        assertThat(memberAhmet.getBirthDate()).isNotNull();
        assertThat(memberReal.getBirthDate()).isNull();

        assertThatThrownBy(() -> memberService.findMember("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Member> memberOptional = memberService.findMember("id1");

        assertThat(memberOptional).as("Member")
                .isPresent()
                .get()
                .matches(member -> member.isTakeBook(book))
        ;

        verify(bookService).findBook(book);
        verify(bookService, times(1)).findBook(book);
        verify(bookService, atLeast(1)).findBook(book);
        verify(bookService, atMost(1)).findBook(book);

      verify(memberRepository, times(3)).findById("id1");
        verify(attendantService).findAttendant(any(Library.class), any(Semester.class));

       verify(attendant).attendantLibraryRecord(argThat(argument -> argument.getCode().equals("111")), any(Semester.class));
     verify(attendant).attendantLibraryRecord(argThat(new MyLibraryArgumentMatcher()), any(Semester.class));
    }
    @Test
    void addBookWithPartialMock() {

        Library library = new Library("111");
        Book book = new Book("121");
        when(bookService.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Member fMember = new Member("f12", "ahmet", "kara");
        when(bookService.findLibraryByBook(book)).thenReturn(library);
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(attendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(attendantService.findAttendant(library, semester)).thenReturn(Optional.of(attendant));
        final Member memberAhmet = mock(Member.class);
        when(memberAhmet.isTakeBook(any(Book.class))).thenReturn(true);
        when(memberRepository.findById(anyString()))
                .thenReturn(Optional.of(memberAhmet)).
                thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(memberAhmet));

        assertThat(memberAhmet.gpa()).isNull();
        doCallRealMethod().when(memberAhmet).gpa();
//        when().thenCallRealMethod();
        assertThatNullPointerException().isThrownBy(memberAhmet::gpa);

        memberService.addBook("id1", book, semester);

        assertThatThrownBy(() -> memberService.findMember("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Member> memberOptional = memberService.findMember("id1");

        assertThat(memberOptional).as("Member")
                .isPresent()
                .get()
                .matches(member -> member.isTakeBook(book))
        ;

        verify(bookService).findBook(book);
        verify(bookService, times(1)).findBook(book);
        verify(bookService, atLeast(1)).findBook(book);
        verify(bookService, atMost(1)).findBook(book);

        verify(memberRepository, times(3)).findById("id1");

        verify(attendantService).findAttendant(any(Library.class), any(Semester.class));

      verify(attendant).attendantLibraryRecord(argThat(argument -> argument.getCode().equals("111")), any(Semester.class));
        verify(attendant).attendantLibraryRecord(argThat(new MyLibraryArgumentMatcher()), any(Semester.class));

    }
    @Test
    void addBookWithBDD() {


        Library library = new Library("111");
        Book book = new Book("121");
        when(bookService.findBook(book)).thenReturn(Optional.of(book));
        Semester semester = new Semester();
        Member fMember = new Member("f12", "ahmet", "kara");
        when(bookService.findLibraryByBook(book)).thenReturn(library);
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(fMember)).thenThrow(new IllegalArgumentException("Can't find a member"))
                .thenReturn(Optional.of(fMember));;

        when(attendant.attendantLibraryRecord(library,semester)).thenReturn(new AttendantLibraryRecord(library,semester));
        when(attendantService.findAttendant(library, semester)).thenReturn(Optional.of(attendant));

        memberService.addBook("id1", book, semester);

        assertThatThrownBy(() -> memberService.findMember("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Member> memberOptional = memberService.findMember("id1");

        assertThat(memberOptional).as("Member")
                .isPresent()
                .get()
                .matches(member -> member.isTakeBook(book))
        ;

        then(bookService).should().findBook(book);
        then(bookService).should(times(1)).findBook(book);
        then(bookService).should(atLeast(1)).findBook(book);
        then(bookService).should(atMost(1)).findBook(book);

        then(memberRepository).should(times(3)).findById("id1");

        then(attendantService).should().findAttendant(any(Library.class), any(Semester.class));

        then(attendant).should().attendantLibraryRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        then(attendant).should().attendantLibraryRecord(argThat(new MyLibraryArgumentMatcher()), any(Semester.class));
    }
}
