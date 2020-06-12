package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.Mentor;
import com.trevormetcalf.schoolscheduler.model.MentorRepository;

import java.util.List;

/*
    This class is the public facing API for manipulating mentors in the database.
 */

public class MentorViewModel extends AndroidViewModel {

    private MentorRepository mentorRepository;
    private LiveData<List<Mentor>> courseMentors;

    public MentorViewModel(@NonNull Application application, int courseID) {
        super(application);
        mentorRepository = new MentorRepository(application, courseID);
        courseMentors = mentorRepository.getCourseMentors(courseID);
    }

    public void insert(Mentor mentor) {
        mentorRepository.insert(mentor);
    }

    public void update(Mentor mentor) {
        mentorRepository.update(mentor);
    }

    public void delete(Mentor mentor) {
        mentorRepository.delete(mentor);
    }

    public LiveData<List<Mentor>> getCourseMentors(int courseID) {
        return courseMentors;
    }
}
