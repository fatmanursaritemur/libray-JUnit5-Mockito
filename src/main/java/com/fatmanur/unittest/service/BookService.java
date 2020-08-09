package com.fatmanur.unittest.service;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;

import java.util.Optional;

public interface BookService{
        Optional<Book> findBook(Book book);
        Optional<Book> findBook(Library library, String code, String name);
        Library findLibraryByBook(Book book);
        }