package com.trevormetcalf.schoolscheduler.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;
import com.trevormetcalf.schoolscheduler.utility.RequestStatusCode;
import com.trevormetcalf.schoolscheduler.utility.Serializer;
import com.trevormetcalf.schoolscheduler.viewmodel.TermAdapter;
import com.trevormetcalf.schoolscheduler.viewmodel.TermViewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
    This class defines the behavior for the Main Screen.
 */

public class MainActivity extends AppCompatActivity {
    // Define variables for UI elements.
    private TermAdapter termAdapter;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up floating action button to add new terms.
        final FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTermActivity.class);
                startActivityForResult(intent, RequestStatusCode.ADD_TERM_REQUEST);
            }
        });
        // Set up RecyclerView for terms.
        RecyclerView termRecyclerView = findViewById(R.id.term_recycler_view);
        termRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        termRecyclerView.setHasFixedSize(true);

        termAdapter = new TermAdapter();
        termRecyclerView.setAdapter(termAdapter);

        //  Get the view model.
        termViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                // Update RecyclerView
                termAdapter.setTerms(terms);
            }
        });

        // Set title text.
        getSupportActionBar().setTitle("Terms");

        // Handle the term click. Goes to the selected term's detailed view.
        termAdapter.setOnTermClickListener(new TermAdapter.OnTermClickListener() {
            @Override
            public void onTermClick(Term term) {
                Intent intent = new Intent(MainActivity.this, TermDetailActivity.class);
                // Send serialized term
                intent.putExtra(AddTermActivity.EXTRA_TERM, Serializer.serialize(term));
                startActivityForResult(intent, RequestStatusCode.TERM_DETAIL_REQUEST);
            }
        });

    }

    // Process information when returning from other activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    // Adding a term.
        if (requestCode == RequestStatusCode.ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            // Get data from extras.
            String termTitle = data.getStringExtra(AddTermActivity.EXTRA_TERM_TITLE);
            Date dateStart = DateFormatter.toDate(data.getStringExtra(AddTermActivity.EXTRA_DATE_START));
            Date dateEnd = DateFormatter.toDate((data.getStringExtra(AddTermActivity.EXTRA_DATE_END)));
            // Construct a new term and insert into the database.
            Term term = new Term(termTitle, dateStart, dateEnd);
            termViewModel.insert(term);
            // Confirm successful operations with a toast message.
            Toast.makeText(this, "Term saved.", Toast.LENGTH_SHORT).show();

    // Deleting a term.
        } else if (requestCode == RequestStatusCode.TERM_DETAIL_REQUEST && data != null &&
                data.getBooleanExtra(TermDetailActivity.EXTRA_DELETE_TERM, false)) {
            // Search for the correct term to delete.
            List<Object> terms =  Arrays.asList( termAdapter.getAllTerms());
            Term termToDelete = null;
            int termID = data.getIntExtra(TermDetailActivity.EXTRA_TERM_ID, -1);
            for (int i = 0; i < terms.size(); i++) {
                if (((Term) terms.get(i)).getId() == termID) {
                    termToDelete = (Term) terms.get(i);
                    break;
                }
            }
            // Delete term from database.
            if (termToDelete != null) {
                termViewModel.delete(termToDelete);
                Toast.makeText(this, "Term Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error, unable to delete at this time", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
