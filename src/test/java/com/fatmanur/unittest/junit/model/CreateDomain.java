package com.fatmanur.unittest.junit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public interface CreateDomain<T> {

    T createDomain();

    @Test
    default void createDomainShouldBeImplemented() {
        Assertions.assertNotNull(createDomain());
    }
}