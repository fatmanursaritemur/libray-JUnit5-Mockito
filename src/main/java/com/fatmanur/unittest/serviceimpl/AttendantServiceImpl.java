package com.fatmanur.unittest.serviceimpl;

import com.fatmanur.unittest.model.Attendant;
import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.Semester;
import com.fatmanur.unittest.repository.AttendantRepository;
import com.fatmanur.unittest.service.AttendantService;

import java.util.Optional;

public class AttendantServiceImpl implements AttendantService {

    private final AttendantRepository attendantRepository;

    public AttendantServiceImpl(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;
    }

    @Override
    public Optional<Attendant> findAttendant(Library library, Semester semester) {
        if ( library== null || semester == null) {
            throw new IllegalArgumentException("Can't find lecturer without course and semester");
        }
        return attendantRepository.findByLibraryAndSemester(library, semester);
    }
}


