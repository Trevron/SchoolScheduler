package com.trevormetcalf.schoolscheduler.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.model.Mentor;
import com.trevormetcalf.schoolscheduler.model.Note;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.MyReceiver;
import com.trevormetcalf.schoolscheduler.utility.RequestStatusCode;
import com.trevormetcalf.schoolscheduler.utility.Serializer;
import com.trevormetcalf.schoolscheduler.viewmodel.AssessmentAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.AssessmentViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.AssessmentViewModelFactory;
import com.trevormetcalf.schoolscheduler.viewmodel.CourseViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.CourseViewModelFactory;
import com.trevormetcalf.schoolscheduler.viewmodel.MentorAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.MentorViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.MentorViewModelFactory;
import com.trevormetcalf.schoolscheduler.viewmodel.NoteAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.NoteViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.NoteViewModelFactory;

import java.util.Date;
import java.util.List;

/*
    This class defines the behavior for the the course detail screen.
 */

public class CourseDetailActivity extends AppCompatActivity {
    // Define String Extras.
    public static final String EXTRA_COURSE_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE_ID";
    public static final String EXTRA_DELETE_COURSE =
            "com.trevormetcalf.schoolscheduler.EXTRA_DELETE_COURSE";

    // Define variables for UI elements.
    private TextView textViewCourseTitle;
    private TextView textViewCourseDateStart;
    private TextView textViewCourseDateEnd;
    private TextView textViewCourseStatus;

    private Button mentorButton;
    private Button assessmentButton;
    private Button noteButton;

    private MentorAdapter mentorAdapter;
    private MentorViewModel mentorViewModel;

    private AssessmentAdapter assessmentAdapter;
    private AssessmentViewModel assessmentViewModel;

    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;

    private CourseViewModel courseViewModel;

    int courseID;
    private Course currentCourse;
    private Term currentTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Enable back button and set action bar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Course Detail");

        // Match course variables to their views.
        textViewCourseTitle = findViewById(R.id.text_view_course_detail_title);
        textViewCourseDateStart = findViewById(R.id.text_view_course_detail_date_start);
        textViewCourseDateEnd = findViewById(R.id.text_view_course_detail_date_end);
        textViewCourseStatus = findViewById(R.id.text_view_course_status);

        // Get intent and set currentCourse to the serialized course.
        Intent intent = getIntent();
        final String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE);
        currentCourse = (Course) Serializer.deserialize(courseSerial);
        // Set current term.
        final String termSerial = intent.getStringExtra(AddTermActivity.EXTRA_TERM);
        currentTerm = (Term) Serializer.deserialize(termSerial);

        // Update current course title, dates, and status.
        textViewCourseTitle.setText(currentCourse.getTitle());
        textViewCourseDateStart.setText(DateFormatter.formatDate(currentCourse.getDateStart()));
        textViewCourseDateEnd.setText(DateFormatter.formatDate(currentCourse.getDateEnd()));
        textViewCourseStatus.setText(currentCourse.getStatus());
        courseID = currentCourse.getId();

        // Associate buttons with their views.
        mentorButton = (Button) findViewById(R.id.button_add_course_mentor);
        assessmentButton = (Button) findViewById(R.id.button_add_course_assessment);
        noteButton = (Button) findViewById(R.id.button_add_course_note);

        // Setup the add mentor button.
        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, AddMentorActivity.class);
                startActivityForResult(intent, RequestStatusCode.ADD_MENTOR_REQUEST);
            }
        });

        // Setup the add assessment button.
        assessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, AddAssessmentActivity.class);
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, courseSerial);
                startActivityForResult(intent, RequestStatusCode.ADD_ASSESSMENT_REQUEST);
            }
        });

        // Setup the add note button.
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, AddNoteActivity.class);
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, courseSerial);
                startActivityForResult(intent, RequestStatusCode.ADD_COURSE_NOTE_REQUEST);
            }
        });

        // Setup the Recyclerview for mentors.
        RecyclerView mentorRecyclerView = findViewById(R.id.course_mentor_recycler_view);
        mentorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mentorRecyclerView.setHasFixedSize(true);

        mentorAdapter = new MentorAdapter();
        mentorRecyclerView.setAdapter(mentorAdapter);

        mentorViewModel = new ViewModelProvider(this, new MentorViewModelFactory(this.getApplication(),
                courseID)).get(MentorViewModel.class);
        mentorViewModel.getCourseMentors(courseID).observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                mentorAdapter.setMentors(mentors);
            }
        });

        // Swipe left to delete mentors.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mentorViewModel.delete(mentorAdapter.getMentorAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseDetailActivity.this, "Mentor deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mentorRecyclerView);

        // Setup the Recyclerview for assessments.
        RecyclerView assessmentRecyclerView = findViewById(R.id.course_assessment_recycler_view);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentRecyclerView.setHasFixedSize(true);

        assessmentAdapter = new AssessmentAdapter();
        assessmentRecyclerView.setAdapter(assessmentAdapter);

        assessmentViewModel = new ViewModelProvider(this, new AssessmentViewModelFactory(this.getApplication(),
                courseID)).get(AssessmentViewModel.class);
        assessmentViewModel.getCourseAssessments(courseID).observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                assessmentAdapter.setAssessments(assessments);
            }
        });

        // Swipe left to delete assessments.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assessmentViewModel.delete(assessmentAdapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseDetailActivity.this, "Assessment deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(assessmentRecyclerView);

        // Setup the Recyclerview for notes.
        final RecyclerView noteRecyclerView = findViewById(R.id.course_note_recycler_view);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteRecyclerView.setHasFixedSize(true);

        noteAdapter = new NoteAdapter();
        noteRecyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(this.getApplication(),
                courseID)).get(NoteViewModel.class);
        noteViewModel.getCourseNotes(courseID).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setCourseNotes(notes);
            }
        });

        // Swipe left to delete notes. Swipe right to share notes.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note currentNote = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {

                    noteViewModel.delete(currentNote);
                    Toast.makeText(CourseDetailActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Sharing Note.", Toast.LENGTH_SHORT).show();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    String noteBody =
                            currentNote.getTitle()
                            + "\n------------------------------------------\n"
                            + currentNote.getDescription();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, noteBody);
                    startActivity(shareIntent.createChooser(shareIntent, "Testing"));
                    // Refresh recyclerview so note doesn't appear to be deleted.
                    noteAdapter.notifyDataSetChanged();
                }
            }
        }).attachToRecyclerView(noteRecyclerView);

        // CourseViewModel instance to allow updates.
        courseViewModel = new ViewModelProvider(this, new CourseViewModelFactory(this.getApplication(),
                currentTerm.getId())).get(CourseViewModel.class);

        // Handle the mentor card click. Edits the selected mentor.
        mentorAdapter.setOnMentorClickListener(new MentorAdapter.OnMentorClickListener() {
            @Override
            public void onMentorClick(Mentor mentor) {
                Intent intent = new Intent(CourseDetailActivity.this, AddMentorActivity.class);
                // Send serialized mentor
                intent.putExtra(AddMentorActivity.EXTRA_MENTOR, Serializer.serialize(mentor));
                startActivityForResult(intent, RequestStatusCode.EDIT_MENTOR_REQUEST);
            }
        });

        // Handle the note card click. Edits the selected note.
        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(CourseDetailActivity.this, AddNoteActivity.class);
                // Send serialized note
                intent.putExtra(AddNoteActivity.EXTRA_NOTE, Serializer.serialize(note));
                startActivityForResult(intent, RequestStatusCode.EDIT_COURSE_NOTE_REQUEST);
            }
        });

        // Handle the assessment card click. Goes to the assessment detail screen.
        assessmentAdapter.setOnAssessmentClickListener(new AssessmentAdapter.OnAssessmentClickListener() {
            @Override
            public void onAssessmentClick(Assessment assessment) {
                Intent intent = new Intent(CourseDetailActivity.this, AssessmentDetailActivity.class);
                // Send serialized assessment
                intent.putExtra(AddAssessmentActivity.EXTRA_ASSESSMENT, Serializer.serialize(assessment));
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, Serializer.serialize(currentCourse));
                startActivityForResult(intent, RequestStatusCode.ASSESSMENT_DETAIL_REQUEST);
            }
        });

    }

    // Access menu layout.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_detail_menu, menu);
        return true;
    }
    // Perform activity on selected menu option.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_course_menu_item:
                Intent intent = new Intent(CourseDetailActivity.this, AddCourseActivity.class);
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, Serializer.serialize(currentCourse));
                intent.putExtra(AddTermActivity.EXTRA_TERM, Serializer.serialize(currentTerm));
                startActivityForResult(intent, RequestStatusCode.EDIT_COURSE_REQUEST);
                break;

            case R.id.delete_course_menu_item:
                Intent data = new Intent();
                data.putExtra(EXTRA_COURSE_ID, courseID);
                data.putExtra(EXTRA_DELETE_COURSE, true);

                setResult(RESULT_OK, data);
                finish();
                break;

            case R.id.notify_start_course_menu_item:
                // Set notification for course start date.
                String courseSerial = Serializer.serialize(currentCourse);
                Intent intentStart = new Intent(CourseDetailActivity.this, MyReceiver.class);
                // Dummy action to prevent extras from dropping.
                intentStart.setAction(Long.toString(System.currentTimeMillis()));
                intentStart.putExtra(AddCourseActivity.EXTRA_COURSE_DATE_START, courseSerial);
                PendingIntent senderStart = PendingIntent.getBroadcast(CourseDetailActivity.this,
                        RequestStatusCode.SET_COURSE_START_NOTIFICATION, intentStart, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                long triggerStart = currentCourse.getDateStart().getTime();
                alarmManagerStart.set(AlarmManager.RTC_WAKEUP, triggerStart, senderStart);
                Toast.makeText(this, "Notification set.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.notify_end_course_menu_item:
                // Set notification for end date.
                String courseSerial2 = Serializer.serialize(currentCourse);
                Intent intentEnd = new Intent(CourseDetailActivity.this, MyReceiver.class);
                // Dummy action to prevent extras from dropping.
                intentEnd.setAction(Long.toString(System.currentTimeMillis()));
                intentEnd.putExtra(AddCourseActivity.EXTRA_COURSE_DATE_END, courseSerial2);
                PendingIntent senderEnd = PendingIntent.getBroadcast(CourseDetailActivity.this,
                        RequestStatusCode.SET_COURSE_END_NOTIFICATION, intentEnd, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                long triggerEnd = currentCourse.getDateEnd().getTime();
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP, triggerEnd, senderEnd);
                Toast.makeText(this, "Notification set.", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // Process information when returning from other activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    // Adding a mentor.
        if (requestCode == RequestStatusCode.ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String mentorName = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_NAME);
            String email = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_EMAIL);
            String phone = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_PHONE_NUMBER);
            // Insert new mentor and confirm successful operations with a toast message.
            Mentor mentor = new Mentor(courseID, mentorName, phone, email);
            mentorViewModel.insert(mentor);
            Toast.makeText(this, "Mentor saved.", Toast.LENGTH_SHORT).show();

    // Adding an assessment.
        } else if (requestCode == RequestStatusCode.ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String title = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
            String dueDate = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE);
            String assessmentType = data.getStringExtra(AddAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
            // Insert new assessment and confirm successful operations with a toast message.
            Assessment assessment = new Assessment(courseID, title, DateFormatter.toDate(dueDate), assessmentType);
            assessmentViewModel.insert(assessment);
            Toast.makeText(this, "Assessment saved.", Toast.LENGTH_SHORT).show();

    // Adding a note.
        } else if (requestCode == RequestStatusCode.ADD_COURSE_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String title = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_DESCRIPTION);
            // Insert new note and confirm successful operations with a toast message.
            Note note = new Note(courseID, title, description);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show();

    // Editing a course.
        } else if (requestCode == RequestStatusCode.EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String courseTitle = data.getStringExtra(AddCourseActivity.EXTRA_COURSE_TITLE);
            Date dateStart = DateFormatter.toDate(data.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_START));
            Date dateEnd = DateFormatter.toDate((data.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_END)));
            String status = data.getStringExtra(AddCourseActivity.EXTRA_COURSE_STATUS);

            // Update course in database.
            Course course = new Course(currentTerm.getId(), courseTitle, dateStart, dateEnd, status);
            course.setId(courseID);
            courseViewModel.update(course);
            currentCourse = course;

            // Update UI with new information and confirm with a toast message.
            textViewCourseTitle.setText(currentCourse.getTitle());
            textViewCourseDateStart.setText(DateFormatter.formatDate(currentCourse.getDateStart()));
            textViewCourseDateEnd.setText(DateFormatter.formatDate(currentCourse.getDateEnd()));
            textViewCourseStatus.setText(currentCourse.getStatus());
            Toast.makeText(this, "Course edit successful", Toast.LENGTH_SHORT).show();


    // Editing a mentor.
        } else if (requestCode == RequestStatusCode.EDIT_MENTOR_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String mentorName = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_NAME);
            String email = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_EMAIL);
            String phone = data.getStringExtra(AddMentorActivity.EXTRA_MENTOR_PHONE_NUMBER);
            int mentorID = data.getIntExtra(AddMentorActivity.EXTRA_MENTOR_ID, -1);
            // Contruct new mentor, set ID, and update in database.
            Mentor mentor = new Mentor(courseID, mentorName, phone, email);
            mentor.setId(mentorID);
            mentorViewModel.update(mentor);
            // Confirm operations with a toast message.
            Toast.makeText(this, "Mentor updated.", Toast.LENGTH_SHORT).show();

    // Editing a note.
        } else if (requestCode == RequestStatusCode.EDIT_COURSE_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String title = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_NOTE_DESCRIPTION);
            int noteID = data.getIntExtra(AddNoteActivity.EXTRA_NOTE_ID, -1);
            // Construct new note, setID, update in database.
            Note note = new Note(courseID, title, description);
            note.setId(noteID);
            noteViewModel.update(note);
            // Confirm operations with a toast message.
            Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show();
        }
    }
}
