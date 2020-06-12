package com.trevormetcalf.schoolscheduler.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    This is the data access class for mentors. SQL queries are defined here.
*/

@Dao
public interface MentorDao {

    @Insert
    void insert(Mentor mentor);

    @Update
    void update(Mentor mentor);

    @Delete
    void delete(Mentor mentor);

    @Query("SELECT * FROM mentor_table WHERE courseID = :courseID")
    LiveData<List<Mentor>> getCourseMentors(int courseID);

}
