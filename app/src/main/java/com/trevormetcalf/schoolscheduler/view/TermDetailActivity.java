package com.trevormetcalf.schoolscheduler.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.RequestStatusCode;
import com.trevormetcalf.schoolscheduler.utility.Serializer;
import com.trevormetcalf.schoolscheduler.viewmodel.CourseAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.CourseViewModel;
import com.trevormetcalf.schoolscheduler.viewmodel.CourseViewModelFactory;
import com.trevormetcalf.schoolscheduler.viewmodel.TermViewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
    This class defines the behavior for the term detail screen.
 */

public class TermDetailActivity extends AppCompatActivity {
    // Define String Extras.
    public static final String EXTRA_DELETE_TERM =
            "com.trevormetcalf.schoolscheduler.EXTRA_DELETE_TERM";

    public static final String EXTRA_TERM_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_TERM_ID";
    // Define variables for UI elements
    private TextView textViewTermTitle;
    private TextView textViewTermDateStart;
    private TextView textViewTermDateEnd;

    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;
    private Term currentTerm;
    private TermViewModel termViewModel;
    int termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        // Associate variables with their views.
        textViewTermTitle = findViewById(R.id.text_view_term_title);
        textViewTermDateStart = findViewById(R.id.text_view_term_date_start);
        textViewTermDateEnd = findViewById(R.id.text_view_term_date_end);

        // Get intent and set current term to serialized term
        Intent intent = getIntent();
        final String termSerial = intent.getStringExtra(AddTermActivity.EXTRA_TERM);
        currentTerm = (Term) Serializer.deserialize(termSerial);

        // Update current term title and dates
        textViewTermTitle.setText(currentTerm.getTitle());
        textViewTermDateStart.setText(DateFormatter.formatDate(currentTerm.getDateStart()));
        textViewTermDateEnd.setText(DateFormatter.formatDate(currentTerm.getDateEnd()));
        termID = currentTerm.getId();

        // Floating action button to add new course.
        final FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_course);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermDetailActivity.this, AddCourseActivity.class);
                intent.putExtra(AddTermActivity.EXTRA_TERM, termSerial);
                startActivityForResult(intent, RequestStatusCode.ADD_COURSE_REQUEST);
            }
        });

        // Set up RecyclerView for courses.
        RecyclerView courseRecyclerView = findViewById(R.id.course_recycler_view);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setHasFixedSize(true);

        courseAdapter = new CourseAdapter();
        courseRecyclerView.setAdapter(courseAdapter);


        //  Get view model from custom factory.
        courseViewModel = new ViewModelProvider(this, new CourseViewModelFactory(this.getApplication(),
                termID)).get(CourseViewModel.class);
        courseViewModel.getTermCourses(termID).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                // Update RecyclerView
                courseAdapter.setCourses(courses);
            }
        });

        termViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(TermViewModel.class);


        // Handle the course click. Goes to course detail for selected course.
        courseAdapter.setOnCourseClickListener(new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
                // Send serialized course
                intent.putExtra(AddCourseActivity.EXTRA_COURSE, Serializer.serialize(course));
                intent.putExtra(AddTermActivity.EXTRA_TERM, Serializer.serialize(currentTerm));
                startActivityForResult(intent, RequestStatusCode.COURSE_DETAIL_REQUEST);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Term Detail");

    }

    // Process information when returning from other activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    // Adding a course.
        if (requestCode == RequestStatusCode.ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            // Get data from Extras.
            String courseTitle = data.getStringExtra(AddCourseActivity.EXTRA_COURSE_TITLE);
            Date dateStart = DateFormatter.toDate(data.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_START));
            Date dateEnd = DateFormatter.toDate((data.getStringExtra(AddCourseActivity.EXTRA_COURSE_DATE_END)));
            String status = data.getStringExtra(AddCourseActivity.EXTRA_COURSE_STATUS);
            // Construct new course and insert into database.
            Course course = new Course(termID, courseTitle, dateStart, dateEnd, status);
            courseViewModel.insert(course);
            // Confirm successful operation with toast message.
            Toast.makeText(this, "Term saved.", Toast.LENGTH_SHORT).show();

    // Editing a term.
        } else if (requestCode == RequestStatusCode.EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            int termID = data.getIntExtra(AddTermActivity.EXTRA_TERM_ID, -1);
            // Make sure termID was passed.
            if (termID == -1) {
                Toast.makeText(this, "Term was not updated.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get data from extras.
            String termTitle = data.getStringExtra(AddTermActivity.EXTRA_TERM_TITLE);
            Date dateStart = DateFormatter.toDate(data.getStringExtra(AddTermActivity.EXTRA_DATE_START));
            Date dateEnd = DateFormatter.toDate((data.getStringExtra(AddTermActivity.EXTRA_DATE_END)));
            // Construct term, set id and then update in database.
            Term term = new Term(termTitle, dateStart, dateEnd);
            term.setId(termID);
            termViewModel.update(term);

            // Update Term Detail UI with new information.
            textViewTermTitle.setText(termTitle);
            textViewTermDateStart.setText(DateFormatter.formatDate(term.getDateStart()));
            textViewTermDateEnd.setText(DateFormatter.formatDate(term.getDateEnd()));

    // Deleting a course.
        } else if (requestCode == RequestStatusCode.COURSE_DETAIL_REQUEST && data != null &&
                data.getBooleanExtra(CourseDetailActivity.EXTRA_DELETE_COURSE, false)) {

            // Search for correct course to delete.
            List<Object> courses =  Arrays.asList( courseAdapter.getAllCourses());
            Course courseToDelete = null;
            int courseID = data.getIntExtra(CourseDetailActivity.EXTRA_COURSE_ID, -1);
            for (int i = 0; i < courses.size(); i++) {
                if (((Course) courses.get(i)).getId() == courseID) {
                    courseToDelete = (Course) courses.get(i);
                    break;
                }
            }
            // Delete from database.
            if (courseToDelete != null) {
                courseViewModel.delete(courseToDelete);
                Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error, unable to delete at this time", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Access menu layout.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.term_detail_menu, menu);
        return true;
    }
    // Perform activity on selected menu option.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_term_menu_item:
                Intent intent = new Intent(TermDetailActivity.this, AddTermActivity.class);
                intent.putExtra(AddTermActivity.EXTRA_TERM_ID, termID);
                intent.putExtra(AddTermActivity.EXTRA_TERM_TITLE, currentTerm.getTitle());
                intent.putExtra(AddTermActivity.EXTRA_DATE_START, DateFormatter.formatDate(currentTerm.getDateStart()));
                intent.putExtra(AddTermActivity.EXTRA_DATE_END, DateFormatter.formatDate(currentTerm.getDateEnd()));
                startActivityForResult(intent, RequestStatusCode.EDIT_TERM_REQUEST);
                break;

            case R.id.delete_term_menu_item:
                if (courseAdapter.getItemCount() > 0) {
                    Toast.makeText(this, "Can't Delete With Courses Associated", Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_TERM_ID, termID);
                    data.putExtra(EXTRA_DELETE_TERM, true);

                    setResult(RESULT_OK, data);
                    finish();
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
