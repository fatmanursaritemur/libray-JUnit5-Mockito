package com.fatmanur.unittest.repository;

import com.fatmanur.unittest.model.Attendant;
import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AttendantRepository {
    Optional<Attendant> findByLibraryAndSemester(Library library, Semester semester);

}
