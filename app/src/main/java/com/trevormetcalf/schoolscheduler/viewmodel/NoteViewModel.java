package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.Note;
import com.trevormetcalf.schoolscheduler.model.NoteRepository;

import java.util.List;
/*
    This class is the public facing API for manipulating notes in the database.
 */

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> courseNotes;


    public NoteViewModel(@NonNull Application application, int courseID) {
        super(application);
        noteRepository = new NoteRepository(application, courseID);
        courseNotes = noteRepository.getCourseNotes(courseID);
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public LiveData<List<Note>> getCourseNotes(int courseID) {
        return courseNotes;
    }

}
