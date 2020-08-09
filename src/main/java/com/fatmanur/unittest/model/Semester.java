package com.fatmanur.unittest.model;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDate;
@Data
@Entity
public class Semester {

    private final int year;
    private final Term term;
    private int addDropPeriodInWeek = 2;

    public Semester(LocalDate localDate) {
        this.year = localDate.getYear();
        this.term = term(localDate.getMonthValue());
    }

    public Semester() {
        final LocalDate now = LocalDate.now();
        this.year = now.getYear();
        this.term = term(now.getMonthValue());
    }

    private Term term(int monthValue) {

        if (monthValue >= Term.FALL.startMonth || monthValue < Term.SPRING.startMonth) {
            return Term.FALL;
        } else if (monthValue >= Term.SPRING.startMonth && monthValue < Term.SUMMER.startMonth) {
            return Term.SPRING;
        }

        return Term.SUMMER;
    }

    public boolean isActive() {
        return this.equals(new Semester());
    }

    public enum Term {
        FALL(9), SPRING(2), SUMMER(6);

        private int startMonth;

        Term(int startMonth) {
            this.startMonth = startMonth;
        }

        public int getStartMonth() {
            return startMonth;
        }
    }

    public boolean isAddDropAllowed() {
        if (!isActive()) {
            return false;
        }

        final LocalDate endOfAddDropPeriod = LocalDate.of(this.getYear(), this.getTerm().getStartMonth(), 1).plusWeeks(addDropPeriodInWeek);

        return !LocalDate.now().isAfter(endOfAddDropPeriod);
    }



    public void setAddDropPeriodInWeek(int addDropPeriodInWeek) {
        this.addDropPeriodInWeek = addDropPeriodInWeek;
    }

    @Override
    public boolean equals(Object obj) {

        if (!Semester.class.isInstance(obj)) {
            return false;
        }

        final Semester semester = (Semester) obj;

        return semester.getYear() == this.getYear() && semester.getTerm() == this.getTerm();
    }


}


