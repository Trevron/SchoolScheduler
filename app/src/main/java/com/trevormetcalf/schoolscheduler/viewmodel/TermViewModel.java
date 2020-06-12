package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.model.TermRepository;

import java.util.List;

/*
    This class is the public facing API for manipulating terms in the database.
 */

public class TermViewModel extends AndroidViewModel {

    private TermRepository termRepository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
        allTerms = termRepository.getAllTerms();
    }

    public void insert(Term term) {
        termRepository.insert(term);
    }

    public void update(Term term) {
        termRepository.update(term);
    }

    public void delete(Term term) {
        termRepository.delete(term);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Course>> getTermCourses(int termID) {
        return termRepository.getTermCourses(termID);
    }

}
