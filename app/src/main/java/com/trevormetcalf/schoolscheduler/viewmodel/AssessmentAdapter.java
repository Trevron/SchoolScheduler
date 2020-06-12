package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Assessment;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the assessment recyclerview.
 */

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {

    private List<Assessment> assessments = new ArrayList<>();
    private OnAssessmentClickListener listener;

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        Assessment currentAssessment = assessments.get(position);
        holder.textViewTitle.setText(currentAssessment.getTitle());
        holder.textViewType.setText(currentAssessment.getType());
        holder.textViewDueDate.setText(DateFormatter.formatDate(currentAssessment.getDateDue()));
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public Assessment getAssessmentAt(int position) {
        return assessments.get(position);
    }

    // This class associates the views with the correct UI elements.
    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        private TextView textViewType;

        private TextView textViewDueDate;

        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_assessment_title);
            textViewType = itemView.findViewById(R.id.text_view_assessment_type);
            textViewDueDate = itemView.findViewById(R.id.text_view_assessment_due_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onAssessmentClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnAssessmentClickListener {
        void onAssessmentClick(Assessment assessment);
    }

    public void setOnAssessmentClickListener(OnAssessmentClickListener listener) {
        this.listener = listener;
    }

}
