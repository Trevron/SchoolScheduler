package com.trevormetcalf.schoolscheduler.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    This is the mentor repository. It provides an interface between the Adapter class and the Dao.
    Insert, update, and delete are defined to run asynchronously so they do not lock up the application.
 */


public class MentorRepository {

    private MentorDao mentorDao;
    private LiveData<List<Mentor>> courseMentors;

    // Might need a parameterless way to instantiate the repository. Just in case something doesnt work. Delete comment if it works fine!!!
    public MentorRepository (Application application, int courseID) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        mentorDao = database.mentorDao();
        courseMentors = mentorDao.getCourseMentors(courseID);
    }

    public void insert(Mentor mentor) {
        new InsertMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void update(Mentor mentor) {
        new UpdateMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void delete(Mentor mentor) {
        new DeleteMentorAsyncTask(mentorDao).execute(mentor);
    }

    public LiveData<List<Mentor>> getCourseMentors(int courseID) {
        return courseMentors;
    }

    private static class InsertMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private InsertMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.insert(mentors[0]);
            return null;
        }
    }

    private static class UpdateMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private UpdateMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.update(mentors[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private DeleteMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.delete(mentors[0]);
            return null;
        }
    }

}
