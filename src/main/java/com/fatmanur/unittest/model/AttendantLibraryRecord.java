package com.fatmanur.unittest.model;

import lombok.Data;
import org.dom4j.Attribute;

import javax.persistence.Entity;
import java.util.Iterator;

@Data
@Entity
public class AttendantLibraryRecord {
    private final Library library;
    private int operationTime;
    private final Semester semester;
    private Attendant attendant;

    public AttendantLibraryRecord(Library library, Semester semester) {
        this.semester = semester;
        this.library = library;
    }
    Book findBook(Book book) {
        for (Iterator<Book> it = library.getBooks().iterator(); it.hasNext(); ) {
            Book f = it.next();
            if (f.equals(book))
                return book;
        }
return null;
    }
}
