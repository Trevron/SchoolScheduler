package com.trevormetcalf.schoolscheduler.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    This is the term repository. It provides an interface between the Adapter class and the Dao.
    Insert, update, and delete are defined to run asynchronously so they do not lock up the application.
 */


public class TermRepository {
    private TermDao termDao;
    private CourseDao courseDao;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> termCourses;

    public TermRepository(Application application) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        termDao = database.termDao();
        courseDao = database.courseDao();
        allTerms = termDao.getAllTerms();
    }

    public void insert(Term term) {
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void update(Term term) {
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void delete(Term term) {
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Course>> getTermCourses(int termID) {
            return courseDao.getTermCourses(termID);
    }

    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }

}
