package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/*
    This is the base class for courses. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
*/

@Entity(tableName = "course_table",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "id",
                childColumns = "termID"
        ))

public class Course implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int termID;

    private String title;

    private Date dateStart;

    private Date dateEnd;

    private String status;


    public Course(int termID, String title, Date dateStart, Date dateEnd, String status) {
        this.termID = termID;
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
