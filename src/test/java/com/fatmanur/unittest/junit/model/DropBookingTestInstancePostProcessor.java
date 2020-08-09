package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.Member;
import com.fatmanur.unittest.model.Semester;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class DropBookingTestInstancePostProcessor implements TestInstancePostProcessor {

    private static Logger logger = Logger.getLogger(DropBookingTestInstancePostProcessor.class.getName());

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        logger.info("Student Ahmet is provided!");
        final MemberTestWithTestInstancePostProcessor.DropBook dropBook = (MemberTestWithTestInstancePostProcessor.DropBook) testInstance;
        dropBook.nur = new Member("id1", "Nur", "Yilmaz");
        dropBook.notActiveSemester = notActiveSemester();
        dropBook.addDropPeriodOpenSemester = addDropPeriodOpenSemester();
        dropBook.addDropPeriodClosedSemester = addDropPeriodClosedSemester();
    }

    private Semester addDropPeriodOpenSemester() {
        final Semester activeSemester = new Semester();
        final LocalDate semesterDate = LocalDate.of(activeSemester.getYear(), activeSemester.getTerm().getStartMonth(), 1);
        final LocalDate now = LocalDate.now();
        activeSemester.setAddDropPeriodInWeek(Long.valueOf(semesterDate.until(now, ChronoUnit.WEEKS) + 1).intValue());
        return activeSemester;
    }

    private Semester addDropPeriodClosedSemester() {
        final Semester activeSemester = new Semester();
        activeSemester.setAddDropPeriodInWeek(0);
        if (LocalDate.now().getDayOfMonth() == 1) {
            activeSemester.setAddDropPeriodInWeek(-1);
        }
        return activeSemester;
    }

    private Semester notActiveSemester() {
        final Semester activeSemester = new Semester();
        return new Semester(LocalDate.of(activeSemester.getYear() - 1, 1, 1));
    }
}