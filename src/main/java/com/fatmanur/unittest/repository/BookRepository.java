package com.fatmanur.unittest.repository;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository {
    Optional<Book> findByExample(Book book);

    Optional<Book> findByLibraryAndCodeAndName(Library library, String code, String name);
}