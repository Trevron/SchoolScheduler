package com.trevormetcalf.schoolscheduler.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    This is the assessment repository. It provides an interface between the Adapter class and the Dao.
    Insert, update, and delete are defined to run asynchronously so they do not lock up the application.
 */

public class AssessmentRepository {
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> courseAssessments;

    public AssessmentRepository(Application application, int courseID) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        assessmentDao = database.assessmentDao();
        courseAssessments = assessmentDao.getCourseAssessments(courseID);
    }

    public void insert(Assessment assessment) {
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void update(Assessment assessment) {
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void delete(Assessment assessment) {
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public LiveData<List<Assessment>> getCourseAssessments(int courseID) {
        return courseAssessments;
    }

    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insert(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.update(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.delete(assessments[0]);
            return null;
        }
    }

}
