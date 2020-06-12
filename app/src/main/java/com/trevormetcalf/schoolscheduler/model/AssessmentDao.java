package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for assessments. SQL queries are defined here.
*/

@Dao
public interface AssessmentDao {

    @Insert
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessment_table WHERE courseID = :courseID")
    LiveData<List<Assessment>> getCourseAssessments(int courseID);

}
