package com.trevormetcalf.schoolscheduler.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    This is the assessment note repository.
    It provides an interface between the Adapter class and the Dao.
    Insert, update, and delete are defined to run asynchronously so they do not lock up the application.
 */


public class AssNoteRepository {

    private AssNoteDao assNoteDao;
    private LiveData<List<AssNote>> assessmentNotes;

    public AssNoteRepository(Application application, int assessmentID) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        assNoteDao = database.assNoteDao();
        assessmentNotes = assNoteDao.getAssessmentNotes(assessmentID);
    }

    public void insert(AssNote assNote) {
        new InsertNoteAsyncTask(assNoteDao).execute(assNote);
    }

    public void update(AssNote assNote) {
        new UpdateNoteAsyncTask(assNoteDao).execute(assNote);
    }

    public void delete(AssNote assNote) {
        new DeleteNoteAsyncTask(assNoteDao).execute(assNote);
    }

    public LiveData<List<AssNote>> getAssessmentNotes(int assessmentID) {
        return assessmentNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<AssNote, Void, Void> {
        private AssNoteDao assNoteDao;
        private InsertNoteAsyncTask(AssNoteDao assNoteDao) {
            this.assNoteDao = assNoteDao;
        }

        @Override
        protected Void doInBackground(AssNote... assNotes) {
            assNoteDao.insert(assNotes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<AssNote, Void, Void> {
        private AssNoteDao assNoteDao;
        private UpdateNoteAsyncTask(AssNoteDao assNoteDao) {
            this.assNoteDao = assNoteDao;
        }

        @Override
        protected Void doInBackground(AssNote... assNotes) {
            assNoteDao.update(assNotes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<AssNote, Void, Void> {
        private AssNoteDao assNoteDao;
        private DeleteNoteAsyncTask(AssNoteDao assNoteDao) {
            this.assNoteDao = assNoteDao;
        }

        @Override
        protected Void doInBackground(AssNote... assNotes) {
            assNoteDao.delete(assNotes[0]);
            return null;
        }
    }
}
