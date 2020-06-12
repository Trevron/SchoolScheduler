package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.AssNote;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the assessment notes recyclerview.
 */

public class AssNoteAdapter extends RecyclerView.Adapter<AssNoteAdapter.AssNoteHolder> {

    private List<AssNote> assessmentNotes = new ArrayList<>();
    private OnAssNoteClickListener listener;

    @NonNull
    @Override
    public AssNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new AssNoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssNoteHolder holder, int position) {
        AssNote currentAssNote = assessmentNotes.get(position);
        holder.textViewTitle.setText(currentAssNote.getTitle());
        holder.textViewDescription.setText(currentAssNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return assessmentNotes.size();
    }

    public void setAssessmentNotes(List<AssNote> assessmentNotes) {
        this.assessmentNotes = assessmentNotes;
        notifyDataSetChanged();
    }

    public AssNote getAssNoteAt(int position) {
        return assessmentNotes.get(position);
    }

    // This class associates the views with the correct UI elements.
    class AssNoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        private TextView textViewDescription;

        public AssNoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_note_title);
            textViewDescription = itemView.findViewById(R.id.text_view_note_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onAssNoteClick(assessmentNotes.get(position));
                    }
                }
            });
        }
    }

    public interface OnAssNoteClickListener {
        void onAssNoteClick(AssNote assNote);
    }

    public void setOnAssNoteClickListener(OnAssNoteClickListener listener) {
        this.listener = listener;
    }

}
