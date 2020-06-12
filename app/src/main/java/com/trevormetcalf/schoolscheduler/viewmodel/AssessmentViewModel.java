package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.model.AssessmentRepository;

import java.util.List;

/*
    This class is the public facing API for manipulating assessments in the database.
 */

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository assessmentRepository;
    private LiveData<List<Assessment>> courseAssessments;

    public AssessmentViewModel(@NonNull Application application, int courseID) {
        super(application);
        assessmentRepository = new AssessmentRepository(application, courseID);
        courseAssessments = assessmentRepository.getCourseAssessments(courseID);
    }

    public void insert(Assessment assessment) {
        assessmentRepository.insert(assessment);
    }

    public void update(Assessment assessment) {
        assessmentRepository.update(assessment);
    }

    public void delete(Assessment assessment) {
        assessmentRepository.delete(assessment);
    }

    public LiveData<List<Assessment>> getCourseAssessments(int courseID) {
        return courseAssessments;
    }

}
