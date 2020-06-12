package com.trevormetcalf.schoolscheduler.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    This is the course repository. It provides an interface between the Adapter class and the Dao.
    Insert, update, and delete are defined to run asynchronously so they do not lock up the application.
 */


public class CourseRepository {

    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> termCourses;

    public CourseRepository(Application application) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public CourseRepository(Application application, int termID) {
        SchoolDatabase database = SchoolDatabase.getInstance(application);
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
        termCourses = courseDao.getTermCourses(termID);
    }

    public void insert(Course course) {
        new InsertCourseAsyncTask(courseDao).execute(course);
    }

    public void update(Course course) {
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void delete(Course course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Course>> getTermCourses(int termID) {
        return termCourses;
    }


    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insert(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.update(courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.delete(courses[0]);
            return null;
        }
    }
}
