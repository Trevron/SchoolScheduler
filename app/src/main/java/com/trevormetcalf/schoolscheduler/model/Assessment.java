package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

/*
    This is the base class for assessments. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
*/

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = CASCADE
        ))

public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int courseID;

    private String title;

    private Date dateDue;

    private String type;


    public Assessment(int courseID, String title, Date dateDue, String type) {
        this.courseID = courseID;
        this.title = title;
        this.dateDue = dateDue;
        this.type = type;
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

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
