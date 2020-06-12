package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.AssNote;
import com.trevormetcalf.schoolscheduler.model.AssNoteRepository;

import java.util.List;

/*
    This class is the public facing API for manipulating assessment notes in the database.
 */

public class AssNoteViewModel extends AndroidViewModel {

    private AssNoteRepository assNoteRepository;
    private LiveData<List<AssNote>> assessmentNotes;

    public AssNoteViewModel(@NonNull Application application, int assessmentID) {
        super(application);
        assNoteRepository = new AssNoteRepository(application, assessmentID);
        assessmentNotes = assNoteRepository.getAssessmentNotes(assessmentID);
    }

    public void insert(AssNote assNote) {
        assNoteRepository.insert(assNote);
    }

    public void update(AssNote assNote) {
        assNoteRepository.update(assNote);
    }

    public void delete(AssNote assNote) {
        assNoteRepository.delete(assNote);
    }

    public LiveData<List<AssNote>> getAssessmentNotes(int assessmentID) {
        return assessmentNotes;
    }

}
