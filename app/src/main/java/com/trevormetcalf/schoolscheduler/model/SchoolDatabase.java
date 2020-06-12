package com.trevormetcalf.schoolscheduler.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trevormetcalf.schoolscheduler.utility.CustomTypeConverters;

/*
    This class defines the SQLite database using the Room architecture.
    Uses the Dao classes to interface with the different table entities.
    Uses the singleton design pattern to ensure there is only one instance of the database.
 */

@Database(  entities = {
            Assessment.class,
            Course.class,
            Mentor.class,
            Note.class,
            Term.class,
            AssNote.class},
            version = 1)
@TypeConverters({CustomTypeConverters.class})
public abstract class SchoolDatabase extends RoomDatabase {
    private static final String TAG = "SchoolDatabase";

    private static SchoolDatabase instance;

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract MentorDao mentorDao();

    public abstract NoteDao noteDao();

    public abstract TermDao termDao();

    public abstract AssNoteDao assNoteDao();

    public static synchronized SchoolDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SchoolDatabase.class, "school_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;

        private CourseDao courseDao;

        private MentorDao mentorDao;

        private NoteDao noteDao;

        private TermDao termDao;

        private AssNoteDao assNoteDao;

        PopulateDbAsyncTask(SchoolDatabase db) {
            assessmentDao = db.assessmentDao();
            courseDao = db.courseDao();
            mentorDao = db.mentorDao();
            noteDao = db.noteDao();
            termDao = db.termDao();
            assNoteDao = db.assNoteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Insert new term here for initial data
            return null;
        }
    }
}

