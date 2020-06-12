package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Mentor;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the mentor recyclerview.
 */

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorHolder> {

    private List<Mentor> mentors = new ArrayList<>();
    private OnMentorClickListener listener;

    @NonNull
    @Override
    public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorHolder holder, int position) {
        Mentor currentMentor = mentors.get(position);
        holder.textViewName.setText(currentMentor.getName());
        holder.textViewEmail.setText(currentMentor.getEmail());
        holder.textViewPhone.setText(currentMentor.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    public Mentor getMentorAt(int position) {
        return mentors.get(position);
    }

    // This class associates the views with the correct UI elements.
    class MentorHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        private TextView textViewEmail;

        private TextView textViewPhone;

        public MentorHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_mentor_name);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            textViewPhone = itemView.findViewById(R.id.text_view_phone_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onMentorClick(mentors.get(position));
                    }
                }
            });
        }
    }

    public interface OnMentorClickListener {
        void onMentorClick(Mentor mentor);
    }

    public void setOnMentorClickListener(OnMentorClickListener listener) {
        this.listener = listener;
    }


}
