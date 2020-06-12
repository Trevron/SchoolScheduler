package com.trevormetcalf.schoolscheduler.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Mentor;
import com.trevormetcalf.schoolscheduler.utility.Serializer;

/*
    This class defines the behavior for adding or editing mentors.
 */

public class AddMentorActivity extends AppCompatActivity {
    // Define String Extras.
    public static final String EXTRA_MENTOR =
            "com.trevormetcalf.schoolscheduler.EXTRA_MENTOR";
    public static final String EXTRA_MENTOR_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_MENTOR_ID";
    public static final String EXTRA_MENTOR_NAME =
            "com.trevormetcalf.schoolscheduler.EXTRA_MENTOR_NAME";
    public static final String EXTRA_MENTOR_EMAIL =
            "com.trevormetcalf.schoolscheduler.EXTRA_MENTOR_EMAIL";
    public static final String EXTRA_MENTOR_PHONE_NUMBER =
            "com.trevormetcalf.schoolscheduler.EXTRA_MENTOR_PHONE_NUMBER";
    // Define UI variables.
    private EditText editTextMentorName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private Mentor currentMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);
        // Associated variables with their views.
        editTextMentorName = findViewById(R.id.edit_text_mentor_name);
        editTextEmail = findViewById(R.id.edit_text_mentor_email);
        editTextPhoneNumber = findViewById(R.id.edit_text_mentor_phone_number);

        // Set back button to a close icon.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        // Check to see if the user is creating or editing a mentor.
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MENTOR)) {
            setTitle("Edit Mentor");
            // Set values for UI elements to data from current mentor.
            String serialMentor = intent.getStringExtra(EXTRA_MENTOR);
            currentMentor = (Mentor) Serializer.deserialize(serialMentor);
            editTextMentorName.setText(currentMentor.getName());
            editTextEmail.setText(currentMentor.getEmail());
            editTextPhoneNumber.setText(currentMentor.getPhoneNumber());
        } else {
            setTitle("Add Mentor");
        }
    }

    public void saveMentor() {
        String name = editTextMentorName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhoneNumber.getText().toString();
        // Validate whether data has been entered into all required fields.
        if (name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
            Toast.makeText(this, "Please insert all information.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Pass data back to CourseDetailActivity
        Intent data = new Intent();
        data.putExtra(EXTRA_MENTOR_NAME, name);
        data.putExtra(EXTRA_MENTOR_EMAIL , email);
        data.putExtra(EXTRA_MENTOR_PHONE_NUMBER, phone);

        if (currentMentor != null) {
            data.putExtra(EXTRA_MENTOR_ID, currentMentor.getId());
        }
        setResult(RESULT_OK, data);
        finish();
    }

    // Set save icon in top right corner.
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
                saveMentor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
