package com.fatmanur.unittest.model;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
@Data
@Entity
public class MemberBookRecord {

    private final AttendantLibraryRecord attendantLibraryRecord;
    private Grade usegeScore;
    private BookReview bookReview;
    private Member member;
    private Book book;

    public MemberBookRecord(AttendantLibraryRecord attendantLibraryRecord, Book book) {
        this.attendantLibraryRecord = attendantLibraryRecord;
        this.book=book;
    }


    public enum Grade {

        A1(BigDecimal.valueOf(4)),
        A2(BigDecimal.valueOf(3.5)),
        B1(BigDecimal.valueOf(3)),
        B2(BigDecimal.valueOf(2.5)),
        C(BigDecimal.valueOf(2)),
        D(BigDecimal.valueOf(1.5)),
        E(BigDecimal.ONE),
        F(BigDecimal.ZERO);

        private BigDecimal gradeInNumber;

        Grade(BigDecimal gradeInNumber) {
            this.gradeInNumber = gradeInNumber;
        }

        public BigDecimal getGradeInNumber() {
            return gradeInNumber;
        }
    }
}
