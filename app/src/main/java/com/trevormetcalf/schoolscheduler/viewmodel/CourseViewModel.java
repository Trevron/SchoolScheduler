package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.model.CourseRepository;

import java.util.List;

/*
    This class is the public facing API for manipulating courses in the database.
 */

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository courseRepository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> termCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCourses();
    }

    public CourseViewModel(@NonNull Application application, int termID) {
        super(application);
        courseRepository = new CourseRepository(application, termID);
        allCourses = courseRepository.getAllCourses();
        termCourses = courseRepository.getTermCourses(termID);
    }

    public void insert(Course course) {
        courseRepository.insert(course);
    }

    public void update(Course course) {
        courseRepository.update(course);
    }

    public void delete(Course course) {
        courseRepository.delete(course);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Course>> getTermCourses(int termID) {
        return termCourses;
    }

}
