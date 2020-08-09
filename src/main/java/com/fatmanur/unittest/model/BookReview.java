package com.fatmanur.unittest.model;

public class BookReview {
    private BookRate BookRate;
    private String comments;
    private MemberBookRecord memberBookRecord;

    public enum BookRate {
        ONE, TWO, THREE, FOUR, FIVE
    }
}
