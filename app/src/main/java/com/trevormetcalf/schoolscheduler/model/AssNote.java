package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

/*
    This is the base class for assessment notes. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
    Perhaps inappropriately named, I thought it was funny. This is separate from the note class
    because I didn't want to deal with missing foreign keys.
*/

@Entity(tableName = "assessment_note_table",
        foreignKeys = {
                @ForeignKey(
                        entity = Assessment.class,
                        parentColumns = "id",
                        childColumns = "assessmentID",
                        onDelete = CASCADE)
        })
public class AssNote implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int assessmentID;

    private String title;

    private String description;

    public AssNote(int assessmentID, String title, String description) {
        this.assessmentID = assessmentID;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
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
