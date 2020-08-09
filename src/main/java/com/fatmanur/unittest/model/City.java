package com.fatmanur.unittest.model;

import lombok.Data;

import java.util.Set;
@Data
public class City { // faculty
    private String code;
    private String name;
    private Set<Library> libraries;
}
