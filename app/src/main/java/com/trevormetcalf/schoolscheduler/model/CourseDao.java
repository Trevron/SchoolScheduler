package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for courses. SQL queries are defined here.
*/

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course_table WHERE id = :courseID")
    LiveData<Course> getCourse(int courseID);

    @Query("SELECT * FROM course_table")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE termID = :termID")
    LiveData<List<Course>> getTermCourses(int termID);

    @Query("SELECT * FROM assessment_table WHERE courseID = :courseID")
    LiveData<List<Assessment>> getCourseAssessments(int courseID);

    @Query("SELECT * FROM note_table WHERE courseID = :courseID")
    LiveData<List<Note>> getCourseNotes(int courseID);

    @Query("SELECT * FROM mentor_table WHERE courseID = :courseID")
    LiveData<List<Mentor>> getCourseMentors(int courseID);



}
