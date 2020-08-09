package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.OS.MAC;

public class CondtionalMemberTest {
    @EnabledOnOs({MAC})
    @Test
    void shouldCreateMemberOnlyOnMac() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledOnOs({MAC})
    @Test
    void shouldCreateMemberOnlyOnNonMac() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledOnJre(JRE.JAVA_10)
    @Test
    void shouldCreateMemberOnlyOnJRE10() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledOnJre({JRE.JAVA_9, JRE.JAVA_10})
    @Test
    void shouldCreateMemberOnlyOnJRE9orJRE10() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledOnJre(JRE.JAVA_10)
    @Test
    void shouldCreateMemberOnlyOnNonJRE10() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    @Test
    void shouldCreateMemberOnlyOn64Architectures() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIfSystemProperty(named = "ENV", matches = "dev")
    @Test
    void shouldCreateMemberOnlyOnDevMachine() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }


    @EnabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    @Test
    void shouldCreateMemberOnlyOnStagingEnv() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    @Test
    void shouldCreateMemberOnlyOnNonCIEnv() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIf("2 * 3 == 6") // Static JavaScript expression.
    @Test
    void shouldCreateMemberIfStaticJSExpressionIsEvaluatedToTrue() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIf("Math.random() < 0.314159") // Dynamic JavaScript expression.
    @Test
    void shouldCreateMemberIfDynamicJSExpressionIsEvaluatedToTrue() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIf("/64/.test(systemProperty.get('os.arch'))") // Regular expression testing bound system property.
    @Test
    void shouldCreateMemberOnlyOn32BitArchitectures() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIf("'staging-server' == systemEnvironment.get('ENV')")
    @Test
    void shouldCreateMemberOnlyOnStagingEnvEvaluatedWithJS() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @TestOnMacWithJRE10
    void shouldCreateMemberOnlyOnMacWithJRE10() {
        final Member ahmet = new Member("1", "Ahmet", "Yılmaz");
        assertAll("Member Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Yılmaz", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledOnOs(MAC)
    @EnabledOnJre(JRE.JAVA_9)
    @interface TestOnMacWithJRE10 {
    }
}
