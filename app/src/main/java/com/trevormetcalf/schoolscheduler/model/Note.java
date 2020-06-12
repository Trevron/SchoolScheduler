package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

/*
    This is the base class for notes. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
*/

@Entity(tableName = "note_table",
        foreignKeys = {
        @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = CASCADE)
        })

public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int courseID;

    private String title;

    private String description;


    public Note(int courseID, String title, String description) {
        this.courseID = courseID;
        this.title = title;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
