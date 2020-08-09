package com.fatmanur.unittest.service;

import com.fatmanur.unittest.model.Attendant;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.Semester;

import java.util.Optional;

public interface AttendantService {
    Optional<Attendant> findAttendant(Library library, Semester semester);
}