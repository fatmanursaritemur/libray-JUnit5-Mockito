package com.fatmanur.unittest.service;

import com.fatmanur.unittest.model.Book;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.MemberBookRecord;
import com.fatmanur.unittest.model.Semester;
import lombok.Data;

@Data
public class TranscriptItem {

    private Library library;
    private int credit;
    private Semester semester;
    private MemberBookRecord.Grade grade;

    public static TranscriptItemBuilder newTranscriptItem() {
        return new TranscriptItemBuilder(new TranscriptItem());
    }

    static class TranscriptItemBuilder {
        private TranscriptItem transcriptItem;

        public TranscriptItemBuilder(TranscriptItem transcriptItem) {
            this.transcriptItem = transcriptItem;
        }

        public TranscriptItemBuilder withLibrary(Library library) {
            this.transcriptItem.setLibrary(library);
            return this;
        }

        public TranscriptItemBuilder withCredit(int credit) {
            this.transcriptItem.setCredit(credit);
            return this;
        }

        public TranscriptItemBuilder withSemester(Semester semester) {
            this.transcriptItem.setSemester(semester);
            return this;
        }

        public TranscriptItemBuilder withGrade(MemberBookRecord.Grade grade) {
            this.transcriptItem.setGrade(grade);
            return this;
        }

        public TranscriptItem transcriptItem() {
            return this.transcriptItem;
        }
    }

}
