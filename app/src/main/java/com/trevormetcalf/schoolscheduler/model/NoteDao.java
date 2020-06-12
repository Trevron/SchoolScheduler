package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for notes. SQL queries are defined here.
*/

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM note_table WHERE courseID = :courseID")
    LiveData<List<Note>> getCourseNotes(int courseID);


}
