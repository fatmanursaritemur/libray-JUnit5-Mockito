package com.fatmanur.unittest.model;

public class BookBuilder {
    private final Book book;

    public BookBuilder(Book book) {
        this.book = book;
    }

    public BookBuilder withCode(String code) {
        this.book.setCode(code);
        return this;
    }

    public BookBuilder withName(String name) {
        this.book.setName(name);
        return this;
    }

    public BookBuilder withTopic(String topic) {
        this.book.setTopic(topic);
        return this;
    }

    public BookBuilder withBookType(Book.BookType bookType) {
        this.book.setBookType(bookType);
        return this;
    }

    public BookBuilder withNumberOfPage(int pagen) {
        this.book.setNumberofpage(pagen);
        return this;
    }

    public BookBuilder withLibrary(Library library) {
        this.book.setLibrary(library);
        return this;
    }

    public Book book() {
        return this.book;
    }
}
