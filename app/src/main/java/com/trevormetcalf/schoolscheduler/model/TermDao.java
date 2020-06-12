package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for terms. SQL queries are defined here.
*/

@Dao
public interface TermDao {

    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM term_table ORDER BY dateStart")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM course_table WHERE termID = :termID")
    LiveData<List<Course>> getTermCourses(int termID);


}
