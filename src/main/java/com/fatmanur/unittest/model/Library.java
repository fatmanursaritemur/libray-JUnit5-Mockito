package com.fatmanur.unittest.model;

import lombok.Data;

import java.util.Set;
@Data
public class Library { // department
    private String code;
    private String name;
    private Set<Attendant> attendants;
    private Set<Book> books;
    private Set<Member> members;
    private City city;
    public Library(String code) {
        this.code = code;
    }
    public Library() {
    }
}
