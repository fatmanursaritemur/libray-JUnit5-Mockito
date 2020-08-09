package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberTestWithTestLifecyle {
    private Member mehmet = new Member("id1", "Mehmet", "Yilmaz");

    @BeforeAll
    static void  setUp() {

    }

    @Test
    void stateCannotChangeWhenLifecyleIsPerMethod() {
        assertEquals("Mehmet", mehmet.getName());
        mehmet = new Member("id2", "Ahmet", "Yilmaz");
    }

    @Test
    void stateCanChangeWhenLifecyleIsPerClass() {
        assertEquals("Mehmet", mehmet.getName());
        mehmet = new Member("id2", "Ahmet", "Yilmaz");
    }
}
