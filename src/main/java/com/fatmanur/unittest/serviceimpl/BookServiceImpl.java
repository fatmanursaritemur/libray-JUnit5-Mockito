package com.fatmanur.unittest.serviceimpl;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.repository.BookRepository;
import com.fatmanur.unittest.service.BookService;

import java.util.Optional;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> findBook(Book book) {
        if(book ==null)
        {
            throw new IllegalArgumentException("Can't find book without book");

        }
        return  bookRepository.findByExample(book);
        }

    @Override
    public Optional<Book> findBook(Library library, String code, String name) {
        if ( library== null || code == null || name==null) {
            throw new IllegalArgumentException("Can't find book without library and code and name");
        }
        return bookRepository.findByLibraryAndCodeAndName(library, code,name);
    }

    @Override
    public Library findLibraryByBook(Book book) {
      Optional<Book> findbook=  findBook(book);
      if(book==null)
      {
          throw new IllegalArgumentException("Can't find book");

      }
      return book.getLibrary();
    }
}
