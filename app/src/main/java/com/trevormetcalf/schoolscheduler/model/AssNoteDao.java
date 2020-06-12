package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for assessment notes. SQL queries are defined here.
*/

@Dao
public interface AssNoteDao {

    @Insert
    void insert(AssNote assNote);

    @Update
    void update(AssNote assNote);

    @Delete
    void delete(AssNote assNote);

    @Query("SELECT * FROM assessment_note_table WHERE assessmentID = :assessmentID")
    LiveData<List<AssNote>> getAssessmentNotes(int assessmentID);

}
