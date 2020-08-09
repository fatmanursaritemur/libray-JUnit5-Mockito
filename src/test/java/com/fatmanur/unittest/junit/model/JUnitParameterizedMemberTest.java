package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Member Test with TestInfo and TestReporter Parameters")
public  class JUnitParameterizedMemberTest {

    private Member member;

    public JUnitParameterizedMemberTest(TestInfo testInfo) {
        assertEquals("Member Test with TestInfo and TestReporter Parameters", testInfo.getDisplayName());
    }

    @BeforeEach
    void setMember(TestInfo testInfo) {

        if (testInfo.getTags().contains("create")) {
            member = new Member("id1", "Ahmet", "Yilmaz");
        } else {
            member = new Member("id1", "Mehmet", "Yilmaz");
        }
    }

    @Test
    @DisplayName("Create Member")
    @Tag("create")
    void createMember(TestInfo testInfo) {
        assertTrue(testInfo.getTags().contains("create"));
        assertEquals("Ahmet", member.getName());
    }

    @Test
    @DisplayName("Add Course to Member")
    @Tag("addCourse")
    void addCourseToMember(TestReporter reporter) {

        reporter.publishEntry("startTime", LocalDateTime.now().toString());
        assertEquals("Mehmet", member.getName());
        reporter.publishEntry("endTime", LocalDateTime.now().toString());
    }
}
