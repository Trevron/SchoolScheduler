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
import com.trevormetcalf.schoolscheduler.model.Note;
import com.trevormetcalf.schoolscheduler.utility.Serializer;

/*
    This class defines the behavior for adding or editing course notes.
 */

public class AddNoteActivity extends AppCompatActivity {
    // Define String Extras.
    public static final String EXTRA_NOTE_ID =
            "com.trevormetcalf.schoolscheduler.EXTRA_NOTE_ID";
    public static final String EXTRA_NOTE_TITLE =
            "com.trevormetcalf.schoolscheduler.EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_DESCRIPTION =
            "com.trevormetcalf.schoolscheduler.EXTRA_NOTE_DESCRIPTION";
    public static final String EXTRA_NOTE =
            "com.trevormetcalf.schoolscheduler.EXTRA_NOTE";
    // Define variables for UI elements.
    private EditText editTextNoteTitle;
    private EditText editTextNoteDescription;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        // Set the back button as a close icon.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
        // Associate variables with their views.
        editTextNoteTitle = findViewById(R.id.edit_text_note_title);
        editTextNoteDescription = findViewById(R.id.edit_text_note_description);

        Intent intent = getIntent();
        // Check to see if the user is editing or creating a note.
        if (intent.hasExtra(EXTRA_NOTE)) {
            // Set values of UI elements to current note.
            final String noteSerial = intent.getStringExtra(EXTRA_NOTE);
            currentNote = (Note) Serializer.deserialize(noteSerial);
            editTextNoteTitle.setText(currentNote.getTitle());
            editTextNoteDescription.setText(currentNote.getDescription());
            setTitle("Edit Note");
        }
    }

    private void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String description = editTextNoteDescription.getText().toString();
        // Validate whether or not all required data has been entered.
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter title and description.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Pass data back to CourseDetailActivity.
        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TITLE, title);
        data.putExtra(EXTRA_NOTE_DESCRIPTION, description);
        if (currentNote != null) {
            data.putExtra(EXTRA_NOTE_ID, currentNote.getId());
        }
        setResult(RESULT_OK, data);
        finish();

    }

    // Add save icon to the top right corner.
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
