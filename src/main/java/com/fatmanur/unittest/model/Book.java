package com.fatmanur.unittest.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Data

public class Book { // Course
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String code;

    private String name;
    private String topic;
    private BookType bookType;
    private int numberofpage;
    private Library Library;
    public Book() {
    }

    public Book(String code) {
        this.code = code;
    }

    public static Book createNewCourse(String courseCode) {
        return new Book(courseCode);
    }
    public static BookBuilder newBook() {
        return new BookBuilder(new Book());
    }
    @Override
    public boolean equals(Object obj) {

        if (!Book.class.isInstance(obj)) {
            return false;
        }

        final Book book2 = (Book) obj;
        return book2.getCode().equals(this.getCode());
    }
    public enum BookType {
        HistoricalFiction,
        Horror,
        Classic,
        SciFi,
        Poetry
    }
// HASH CODE VE TO STRİNG FARKLIIIII

}
