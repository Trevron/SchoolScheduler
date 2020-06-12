package com.trevormetcalf.schoolscheduler.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/*
    This is the base class for terms. The Room architecture uses @Entity to define a
    table in the database. Contains typical constructor as well as getter and setter methods.
*/

@Entity(tableName = "term_table")
public class Term implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private Date dateStart;

    private Date dateEnd;

    public Term(String title, Date dateStart, Date dateEnd) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Term(String title, Date dateStart, Date dateEnd, int termID) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.id = termID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


}
