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
import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.Serializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    This class defines the behavior for adding and editing assessments.
 */

public class AddAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Define the String Extras.
    public static final String EXTRA_ASSESSMENT =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASSESSMENT";
    public static final String EXTRA_ASSESSMENT_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_TITLE =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_DUE_DATE =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASSESSMENT_DUE_DATE";
    public static final String EXTRA_ASSESSMENT_TYPE =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASSESSMENT_TYPE";

    // Define variables for UI elements.
    DatePickerDialog datePicker;
    private EditText editTextAssessmentTitle;
    private EditText editTextAssessmentDueDate;
    private Spinner spinnerAssessmentType;
    private Course currentCourse;

    String assessmentType = null;

    private Assessment currentAssessment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        // Add an 'X' icon as the back button.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Assessment");
        // Associate variables with their views.
        editTextAssessmentTitle = findViewById(R.id.edit_text_assessment_title);
        editTextAssessmentDueDate = findViewById(R.id.edit_text_assessment_due_date);


        // Pass course information from intent.
        Intent intent = getIntent();
        final String courseSerial = intent.getStringExtra(AddCourseActivity.EXTRA_COURSE);
        currentCourse = (Course) Serializer.deserialize(courseSerial);
        final Date startDate = currentCourse.getDateStart();
        final Date endDate = currentCourse.getDateEnd();

        // Initialize and populate the spinner.
        spinnerAssessmentType = findViewById(R.id.spinner_assessment_type);
        spinnerAssessmentType.setOnItemSelectedListener(this);
        List<String> assessmentTypes = new ArrayList<String>();
        assessmentTypes.add("Performance Assessment");
        assessmentTypes.add("Objective Assessment");
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assessmentTypes);
        spinnerAssessmentType.setAdapter(spinAdapter);

        // Prevent text input on the "Due Date" form.
        editTextAssessmentDueDate.setInputType(InputType.TYPE_NULL);
        // Use onFocusChangeListener to detect if due date has been clicked.
        editTextAssessmentDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                datePicker = new DatePickerDialog(AddAssessmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextAssessmentDueDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                // Limit the selectable dates to the date range of the current course.
                datePicker.getDatePicker().setMinDate(startDate.getTime());
                datePicker.getDatePicker().setMaxDate(endDate.getTime());
                datePicker.show();
            }
        });

        // Check if editing or creating new assessment.
        // If Extra exists, set values to the current assessment.
        if (intent.hasExtra(EXTRA_ASSESSMENT)) {
            final String assessmentSerial = intent.getStringExtra(EXTRA_ASSESSMENT);
            currentAssessment = (Assessment) Serializer.deserialize(assessmentSerial);
            editTextAssessmentTitle.setText(currentAssessment.getTitle());
            editTextAssessmentDueDate.setText(DateFormatter.formatDate(currentAssessment.getDateDue()));
            setTitle("Edit Assessment");
        }
    }

    private void saveAssessment() {
        String title = editTextAssessmentTitle.getText().toString();
        String dueDate = editTextAssessmentDueDate.getText().toString();
        // Check to make sure data is entered in all fields.
        if (title.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please ensure all options are filled out.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Send data back to activity. Should have probably used the serializer...
        Intent data = new Intent();
        data.putExtra(EXTRA_ASSESSMENT_TITLE, title);
        data.putExtra(EXTRA_ASSESSMENT_DUE_DATE, dueDate);
        data.putExtra(EXTRA_ASSESSMENT_TYPE, assessmentType);

        setResult(RESULT_OK, data);
        finish();
    }

    // Create a menu.
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
                saveAssessment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Gets the selected item from the spinner and assigns it to assessmentType.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        assessmentType = parent.getItemAtPosition(position).toString();
    }
    // The default value of the spinner if nothing is selected.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        assessmentType = "Performance Assessment";
    }

}
