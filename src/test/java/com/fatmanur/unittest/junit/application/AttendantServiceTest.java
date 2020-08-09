package com.fatmanur.unittest.junit.application;

import com.fatmanur.unittest.model.Attendant;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.Semester;
import com.fatmanur.unittest.repository.AttendantRepository;
import com.fatmanur.unittest.service.AttendantService;
import com.fatmanur.unittest.serviceimpl.AttendantServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AttendantServiceTest {

    @Test
    void findAttendant() {
            final Library library = new Library("F12");
            final Semester semester = new Semester();

            final AttendantRepository attendantRepository = Mockito.mock(AttendantRepository.class);
            final Attendant attendant = new Attendant();
            Mockito.when(attendantRepository.findByLibraryAndSemester(library, semester)).thenReturn(Optional.of(attendant));

            final AttendantService attendantService = new AttendantServiceImpl(attendantRepository);
            final Optional<Attendant> attendantOpt = attendantService.findAttendant(library,semester);
            assertThat(attendantOpt)
                    .isPresent()
                    .get()
                    .isSameAs(attendant)
            ;

            Mockito.verify(attendantRepository).findByLibraryAndSemester(library, semester);
        }
     @Test
    void testMockitoConstraint()
     {
         Mockito.mock(AttendantServiceImpl.class);
     }
}
