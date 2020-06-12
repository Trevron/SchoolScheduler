package com.trevormetcalf.schoolscheduler.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.AssNote;
import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.MyReceiver;
import com.trevormetcalf.schoolscheduler.utility.RequestStatusCode;
import com.trevormetcalf.schoolscheduler.utility.Serializer;
import com.trevormetcalf.schoolscheduler.viewmodel.AssNoteAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.AssNoteViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.AssNoteViewModelFactory;
import com.trevormetcalf.schoolscheduler.viewmodel.AssessmentViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.AssessmentViewModelFactory;

import java.util.List;

/*
    This class defines the behavior for the assessment detail screen.
 */

public class AssessmentDetailActivity extends AppCompatActivity {
    // Define variables for UI elements.
    private TextView textViewTitle;
    private TextView textViewType;
    private TextView textViewDueDate;
    private Button assNoteButton;

    private AssessmentViewModel assessmentViewModel;
    private AssNoteAdapter assNoteAdapter;
    private AssNoteViewModel assNoteViewModel;

    private Assessment currentAssessment;
    private Course currentCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Assessment Detail");

        // Get intent and set current assessment and current course with serialized data.
        Intent intent = getIntent();
        final String assessmentSerial = intent.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT);
        currentAssessment = (Assessment) Serializer.deserialize(assessmentSerial);

        final String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE);
        currentCourse = (Course) Serializer.deserialize(courseSerial);

        // Associate variables with their respective views.
        textViewTitle = findViewById(R.id.text_view_assessment_detail_title);
        textViewType = findViewById(R.id.text_view_assessment_detail_type);
        textViewDueDate = findViewById(R.id.text_view_assessment_detail_due_date);
        assNoteButton = (Button) findViewById(R.id.button_add_assessment_note);

        // Update current assessment title, type and date.
        textViewTitle.setText(currentAssessment.getTitle());
        textViewType.setText(currentAssessment.getType());
        textViewDueDate.setText(DateFormatter.formatDate(currentAssessment.getDateDue()));

        // Set up the add assessment note button.
        assNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentDetailActivity.this, AddAssNoteActivity.class);
                startActivityForResult(intent, RequestStatusCode.ADD_ASS_NOTE_REQUEST);
            }
        });

        // Setup the Assessment Note recyclerview.
        RecyclerView assNoteRecyclerView = findViewById(R.id.assessment_note_recycler_view);
        assNoteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assNoteRecyclerView.setHasFixedSize(true);

        assNoteAdapter = new AssNoteAdapter();
        assNoteRecyclerView.setAdapter(assNoteAdapter);

        assNoteViewModel = new ViewModelProvider(this, new AssNoteViewModelFactory(this.getApplication(),
                currentAssessment.getId())).get(AssNoteViewModel.class);
        assNoteViewModel.getAssessmentNotes(currentAssessment.getId()).observe(this,
                new Observer<List<AssNote>>() {
            @Override
            public void onChanged(List<AssNote> assNotes) {
                assNoteAdapter.setAssessmentNotes(assNotes);
            }
        });

        // Swipe left to delete assessment notes.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assNoteViewModel.delete(assNoteAdapter.getAssNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AssessmentDetailActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(assNoteRecyclerView);

        // Handle the assessment note card click. Edits the selected note.
        assNoteAdapter.setOnAssNoteClickListener(new AssNoteAdapter.OnAssNoteClickListener() {
            @Override
            public void onAssNoteClick(AssNote assNote) {
                Intent intent = new Intent(AssessmentDetailActivity.this, AddAssNoteActivity.class);
                // Send serialized assessment note
                intent.putExtra(AddAssNoteActivity.EXTRA_ASS_NOTE, Serializer.serialize(assNote));
                startActivityForResult(intent, RequestStatusCode.EDIT_ASS_NOTE_REQUEST);
            }
        });
        // Instantiate assessment viewmodel in order to interact use database functions.
        assessmentViewModel = new ViewModelProvider(this, new AssessmentViewModelFactory(this.getApplication(),
                currentCourse.getId())).get(AssessmentViewModel.class);

    }

    // Access menu layout.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.assessment_detail_menu, menu);
        return true;
    }

    // Perform activity on selected menu option.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_assessment_menu_item:
                Intent intent = new Intent(AssessmentDetailActivity.this, AddAssessmentActivity.class);
                intent.putExtra(AddAssessmentActivity.EXTRA_ASSESSMENT, Serializer.serialize(currentAssessment));
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, Serializer.serialize(currentCourse));
                startActivityForResult(intent, RequestStatusCode.EDIT_ASSESSMENT_REQUEST);
                break;

            case R.id.notify_due_date_assessment_menu_item:
                // Set assessment due notification.
                Intent intentNotify = new Intent(AssessmentDetailActivity.this, MyReceiver.class);
                intentNotify.setAction(Long.toString(System.currentTimeMillis()));
                String assessmentSerial = Serializer.serialize(currentAssessment);
                intentNotify.putExtra(AddAssessmentActivity.EXTRA_ASSESSMENT, assessmentSerial);
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailActivity.this,
                        RequestStatusCode.SET_ASSESSMENT_NOTIFICATION, intentNotify, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                long trigger = currentAssessment.getDateDue().getTime();
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                // Notify user that the assessment has been updated.
                Toast.makeText(this, "Notification set.", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // Process information when returning from other activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    // Editing an assessment.
        if (requestCode == RequestStatusCode.EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            // Get data from Extras.
            String title = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
            String dueDate = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE);
            String assessmentType = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
            // Construct assessment, set ID and then update.
            Assessment assessment = new Assessment(currentCourse.getId(), title,
                    DateFormatter.toDate(dueDate), assessmentType);
            assessment.setId(currentAssessment.getId());
            assessmentViewModel.update(assessment);
            // Update UI elements to reflect new data.
            textViewTitle.setText(assessment.getTitle());
            textViewDueDate.setText(DateFormatter.formatDate(assessment.getDateDue()));
            textViewType.setText(assessment.getType());

            currentAssessment = assessment;
            // Notify user that the assessment has been updated.
            Toast.makeText(this, "Assessment Updated.", Toast.LENGTH_SHORT).show();

    // Add assessment note.
        } else if (requestCode == RequestStatusCode.ADD_ASS_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String title = data.getStringExtra(AddAssNoteActivity.EXTRA_ASS_NOTE_TITLE);
            String description = data.getStringExtra(AddAssNoteActivity.EXTRA_ASS_NOTE_DESCRIPTION);
            // Construct new assessment note and insert it into database.
            AssNote assNote = new AssNote(currentAssessment.getId(), title, description);
            assNoteViewModel.insert(assNote);
            // Notify user of successful operations.
            Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show();

    // Edit assessment note.
        } else if (requestCode == RequestStatusCode.EDIT_ASS_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String title = data.getStringExtra(AddAssNoteActivity.EXTRA_ASS_NOTE_TITLE);
            String description = data.getStringExtra(AddAssNoteActivity.EXTRA_ASS_NOTE_DESCRIPTION);
            int noteID = data.getIntExtra(AddAssNoteActivity.EXTRA_ASS_NOTE_ID, -1);
            // Construct assessment, set ID, and then update.
            AssNote assNote = new AssNote(currentAssessment.getId(), title, description);
            assNote.setId(noteID);
            assNoteViewModel.update(assNote);
            // Notify user of successful operation.
            Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show();
        }
    }
}