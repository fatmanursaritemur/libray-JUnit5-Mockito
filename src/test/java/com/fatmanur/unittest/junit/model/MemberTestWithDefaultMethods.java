package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberTestWithDefaultMethods implements CreateDomain<Member>, TestLifecycleReporter {

    @Override
    public Member createDomain() {
        return new Member("123","Ayse","Kara");
    }

    @Test
    void createStudent() {
        final Member member = createDomain();

        assertAll("Student",
                () -> assertEquals("123", member.getId()),
                () -> assertEquals("Ayse", member.getName()),
                () -> assertEquals("Kara", member.getSurname())
        );
    }
}