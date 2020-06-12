package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Note;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the note recyclerview.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> courseNotes = new ArrayList<>();
    private OnNoteClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = courseNotes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return courseNotes.size();
    }

    public void setCourseNotes(List<Note> courseNotes) {
        this.courseNotes = courseNotes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return courseNotes.get(position);
    }

    // This class associates the views with the correct UI elements.
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        private TextView textViewDescription;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_note_title);
            textViewDescription = itemView.findViewById(R.id.text_view_note_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onNoteClick(courseNotes.get(position));
                    }
                }
            });
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

}
