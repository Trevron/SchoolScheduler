package com.trevormetcalf.schoolscheduler.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/*
    This class allows the viewmodel to be created with a parameter that can be used in SQL queries.
 */

public class MentorViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    private int courseID;

    public MentorViewModelFactory(Application application, int courseID) {
        this.application = application;
        this.courseID = courseID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MentorViewModel(application, courseID);
    }
}
