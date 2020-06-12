package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

/*
    This is the base class for mentors. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
*/

@Entity(tableName = "mentor_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = CASCADE
        ))

public class Mentor implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int courseID;

    private String name;

    private String phoneNumber;

    private String email;

    public Mentor(int courseID, String name, String phoneNumber, String email) {
        this.courseID = courseID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
