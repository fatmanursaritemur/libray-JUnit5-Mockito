package com.fatmanur.unittest.model;


import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import static jdk.dynalink.linker.support.Guards.isNotNull;
public class MemberAssert extends AbstractAssert<MemberAssert, Member> {

    public MemberAssert(Member member) {
        super(member, MemberAssert.class);
    }

    public static MemberAssert assertThat(Member actual) {
        return new MemberAssert(actual);
    }

    public MemberAssert hasSurName(String surname) {
        isNotNull();

        if (!Objects.equals(surname, actual.getSurname())) {
            failWithMessage("Expected member's name %s but was found %s", surname, actual.getSurname());
        }

        return this;
    }

}