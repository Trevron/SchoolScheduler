package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Course;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the course recyclerview.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private List<Course> courses = new ArrayList<>();
    private OnCourseClickListener listener;

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course currentCourse = courses.get(position);
        holder.textViewTitle.setText(currentCourse.getTitle());
        holder.textViewDateStart.setText(DateFormatter.formatDate(currentCourse.getDateStart()));
        holder.textViewDateEnd.setText(DateFormatter.formatDate(currentCourse.getDateEnd()));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public Course getCourseAt(int position) {
        return courses.get(position);
    }

    public Object[] getAllCourses() {
        return courses.toArray();
    }

    // This class associates the views with the correct UI elements.
    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        private TextView textViewDateStart;

        private TextView textViewDateEnd;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_course_title);
            textViewDateStart = itemView.findViewById(R.id.text_view_course_date_start);
            textViewDateEnd = itemView.findViewById(R.id.text_view_course_date_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCourseClick(courses.get(position));
                    }
                }
            });
        }
    }

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

}
