package com.fatmanur.unittest.model;

import com.fatmanur.unittest.exception.NotActiveSemesterException;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
@Data
@Entity
public class Attendant { //lecturer
    private String id;
    private String name;
    private String surname;
    private String title;
    private Set<AttendantLibraryRecord> attendantLibraryRecords = new HashSet<>();
    private Library library;


    public void addAttendantLibraryRecord(AttendantLibraryRecord attendantLibraryRecord) {

        if (attendantLibraryRecord.getLibrary() == null) {
            throw new IllegalArgumentException("Can't add a null library to attendant");
        }

        if (!attendantLibraryRecord.getSemester().isActive()) {
            throw new NotActiveSemesterException(attendantLibraryRecord.getSemester().toString());
        }

        attendantLibraryRecord.setAttendant(this);
        this.attendantLibraryRecords.add(attendantLibraryRecord);
    }

    public AttendantLibraryRecord attendantLibraryRecord(Library library, Semester semester) {
        return attendantLibraryRecords.stream()
                .filter(
                        attendantLibraryRecord ->
                                attendantLibraryRecord.getLibrary().equals(library) && attendantLibraryRecord.getSemester().equals(semester)
                )
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find attendant library record for library<%s> and semester<%s>", library, semester)));

    }
}
