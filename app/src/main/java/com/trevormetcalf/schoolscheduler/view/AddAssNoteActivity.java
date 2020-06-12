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
import com.trevormetcalf.schoolscheduler.model.AssNote;
import com.trevormetcalf.schoolscheduler.utility.Serializer;

/*
    This class defines the behavior for adding and editing assessment notes.
 */

public class AddAssNoteActivity extends AppCompatActivity {

    // Define the String Extras.
    public static final String EXTRA_ASS_NOTE_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASS_NOTE_ID";
    public static final String EXTRA_ASS_NOTE_TITLE =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASS_NOTE_TITLE";
    public static final String EXTRA_ASS_NOTE_DESCRIPTION =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASS_NOTE_DESCRIPTION";
    public static final String EXTRA_ASS_NOTE =
            "com.trevormetcalf.schoolscheduler.EXTRA_ASS_NOTE";
    // Define Variables for the UI elements.
    private EditText editTextAssNoteTitle;
    private EditText editTextAssNoteDescription;
    private AssNote currentAssNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ass_note);
        // Set an 'X' icon as the back button.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
        // Associate variables with their variables.
        editTextAssNoteTitle = findViewById(R.id.edit_text_ass_note_title);
        editTextAssNoteDescription = findViewById(R.id.edit_text_ass_note_description);

        // Check to see if user is editing or creating a new note.
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ASS_NOTE)) {
            final String assNoteSerial = intent.getStringExtra(EXTRA_ASS_NOTE);
            currentAssNote = (AssNote) Serializer.deserialize(assNoteSerial);
            editTextAssNoteTitle.setText(currentAssNote.getTitle());
            editTextAssNoteDescription.setText(currentAssNote.getDescription());
            setTitle("Edit Note");
        }

    }

    private void saveNote() {
        String title = editTextAssNoteTitle.getText().toString();
        String description = editTextAssNoteDescription.getText().toString();
        // Validate whether or not data has been entered in the required fields.
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter title and description.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Pass data back to AssessmentDetailActivity.
        Intent data = new Intent();
        data.putExtra(EXTRA_ASS_NOTE_TITLE, title);
        data.putExtra(EXTRA_ASS_NOTE_DESCRIPTION, description);

        if (currentAssNote != null) {
            data.putExtra(EXTRA_ASS_NOTE_ID, currentAssNote.getId());
        }

        setResult(RESULT_OK, data);
        finish();
    }

    // Ended up reusing the same save icon menu for all add/edit activities.
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
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
