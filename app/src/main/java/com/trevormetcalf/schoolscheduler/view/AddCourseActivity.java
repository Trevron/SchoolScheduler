package com.trevormetcalf.schoolscheduler.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.Serializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    This class defines the behavior for adding and editing courses.
 */

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Define String extras.
    public static final String EXTRA_COURSE =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE";
    public static final String EXTRA_COURSE_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_DATE_START =
            "com.trevormetcalf.schoolscheduler.EXTRA_DATE_COURSE_START";
    public static final String EXTRA_COURSE_DATE_END =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE_DATE_END";
    public static final String EXTRA_COURSE_STATUS =
            "com.trevormetcalf.schoolscheduler.EXTRA_COURSE_STATUS";

    // Define variables for UI elements.
    DatePickerDialog datePicker;
    private EditText editTextCourseTitle;
    private EditText editTextCourseDateStart;
    private EditText editTextCourseDateEnd;
    private Spinner spinnerCourseStatus;
    String status = null;
    private Term currentTerm;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        // Associate variables with their views.
        editTextCourseTitle = findViewById(R.id.edit_text_course_title);
        editTextCourseDateStart = findViewById(R.id.edit_text_course_date_start);
        editTextCourseDateEnd = findViewById(R.id.edit_text_course_date_end);

        // Set the back button as the close icon.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Course");

        Intent intent = getIntent();
        // Check to see if a course was passed in. If so, we are editing instead of creating new.
        // Set UI elements with data from current course.
        if (intent.hasExtra(AddCourseActivity.EXTRA_COURSE)) {
            final String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE);
            currentCourse = (Course) Serializer.deserialize(courseSerial);
            editTextCourseTitle.setText(currentCourse.getTitle());
            editTextCourseDateStart.setText(DateFormatter.formatDate(currentCourse.getDateStart()));
            editTextCourseDateEnd.setText(DateFormatter.formatDate(currentCourse.getDateEnd()));
            status = currentCourse.getStatus();
            setTitle("Edit Course");
        }

        //Pass term information to restrict start and end dates.
        final String termSerial = intent.getStringExtra(AddTermActivity.EXTRA_TERM);
        currentTerm = (Term) Serializer.deserialize(termSerial);
        final Date startDate = currentTerm.getDateStart();
        final Date endDate = currentTerm.getDateEnd();

        // Initialize and populate the spinner.
        spinnerCourseStatus = (Spinner) findViewById(R.id.spinner_course_status);
        spinnerCourseStatus.setOnItemSelectedListener(this);
        List<String> status = new ArrayList<String>();
        status.add("In Progress");
        status.add("Completed");
        status.add("Dropped");
        status.add("Plan to Take");

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, status);

        spinnerCourseStatus.setAdapter(spinAdapter);

        // Prevent text from being input into the "Start Date" field.
        // Use onFocusChangeListener to define behavior for when "Start Date" field is clicked.
        editTextCourseDateStart.setInputType(InputType.TYPE_NULL);
        editTextCourseDateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    return;
                }
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date Picker Dialog
                datePicker = new DatePickerDialog(AddCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextCourseDateStart.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                // Limit the selectable dates to the date range of the current term.
                datePicker.getDatePicker().setMinDate(startDate.getTime());
                datePicker.getDatePicker().setMaxDate(endDate.getTime());
                datePicker.show();
            }
        });

        // Prevent text from being input into the "End Date" field.
        // Use onFocusChangeListener to define behavior for when "End Date" field is clicked.
        editTextCourseDateEnd.setInputType(InputType.TYPE_NULL);
        editTextCourseDateEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    return;
                }
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date Picker Dialog
                datePicker = new DatePickerDialog(AddCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextCourseDateEnd.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                // Limit the selectable dates to the date range of the current term.
                datePicker.getDatePicker().setMinDate(startDate.getTime());
                datePicker.getDatePicker().setMaxDate(endDate.getTime());
                datePicker.show();
            }
        });


    }

    private void saveCourse() {
        String title = editTextCourseTitle.getText().toString();
        String dateStart = editTextCourseDateStart.getText().toString();
        String dateEnd = editTextCourseDateEnd.getText().toString();

        // Validate that data has been entered in all required fields.
        if (title.trim().isEmpty() || dateStart.trim().isEmpty() || dateEnd.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert course title, dates and title.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Pass data back to TermDetailActivity. Probably should have used Serializer...
        Intent data = new Intent();
        data.putExtra(EXTRA_COURSE_TITLE, title);
        data.putExtra(EXTRA_COURSE_DATE_START, dateStart);
        data.putExtra(EXTRA_COURSE_DATE_END, dateEnd);
        data.putExtra(EXTRA_COURSE_STATUS, status);
        setResult(RESULT_OK, data);
        finish();
    }

    // Add save icon to top right.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                saveCourse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Get selected item from spinner and set value for status.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status = parent.getItemAtPosition(position).toString();
    }
    // Set default status value if nothing is selected in the spinner.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        status = "In Progress";
    }
}
