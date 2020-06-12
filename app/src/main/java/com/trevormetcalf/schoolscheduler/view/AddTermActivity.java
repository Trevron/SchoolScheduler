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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.trevormetcalf.schoolscheduler.R;

/*
    This class defines the behavior for adding and editing terms.
 */

public class AddTermActivity extends AppCompatActivity {
    // Define String Extras.
    public static final String EXTRA_TERM_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE =
            "com.trevormetcalf.schoolscheduler.EXTRA_TERM_TITLE";
    public static final String EXTRA_DATE_START =
            "com.trevormetcalf.schoolscheduler.EXTRA_DATE_START";
    public static final String EXTRA_DATE_END =
            "com.trevormetcalf.schoolscheduler.EXTRA_DATE_END";
    public static final String EXTRA_TERM =
            "com.trevormetcalf.schoolscheduler.EXTRA_TERM";
    // Define variables for UI elements.
    DatePickerDialog datePicker;
    private EditText editTextTermTitle;
    private EditText editTextDateStart;
    private EditText editTextDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        // Set the back button to a close icon.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // Associate variables with their views.
        editTextTermTitle = findViewById(R.id.edit_text_term_title);
        editTextDateStart = findViewById(R.id.edit_text_date_start);
        editTextDateEnd = findViewById(R.id.edit_text_date_end);

        // Prevent text from being entered in the "Start Date" field.
        // Use onFocusChangeListener to define behavior for when "Start Date" field is clicked.
        editTextDateStart.setInputType(InputType.TYPE_NULL);
        editTextDateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                datePicker = new DatePickerDialog(AddTermActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextDateStart.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        // Prevent text from being entered in the "End Date" field.
        // Use onFocusChangeListener to define behavior for when "End Date" field is clicked.
        editTextDateEnd.setInputType(InputType.TYPE_NULL);
        editTextDateEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                datePicker = new DatePickerDialog(AddTermActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextDateEnd.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        // Check to see if the user is editing or creating a new term.
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TERM_ID)) {
            // Set the values of the UI elements to the current term.
            setTitle("Edit Term");
            editTextTermTitle.setText(intent.getStringExtra(EXTRA_TERM_TITLE));
            editTextDateStart.setText(intent.getStringExtra(EXTRA_DATE_START));
            editTextDateEnd.setText(intent.getStringExtra(EXTRA_DATE_END));
        } else {
            setTitle("Add Term");
        }
    }

    private void saveTerm() {
        String title = editTextTermTitle.getText().toString();
        String dateStart = editTextDateStart.getText().toString();
        String dateEnd = editTextDateEnd.getText().toString();
        // Validate whether or not data has been entered for the required fields.
        if (title.trim().isEmpty() || dateStart.trim().isEmpty() || dateEnd.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title and dates", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass data back to MainActivity.
        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, title);
        data.putExtra(EXTRA_DATE_START, dateStart);
        data.putExtra(EXTRA_DATE_END, dateEnd);
        // If editing term, termID will exist.
        int termID = getIntent().getIntExtra(EXTRA_TERM_ID, -1);
        if (termID != -1) {
            data.putExtra(EXTRA_TERM_ID, termID);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    // Add save icon to top right corner.
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
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}















