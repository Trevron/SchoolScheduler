package com.trevormetcalf.schoolscheduler.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trevormetcalf.schoolscheduler.R;
import com.trevormetcalf.schoolscheduler.model.Term;
import com.trevormetcalf.schoolscheduler.utility.DateFormatter;

import java.util.ArrayList;
import java.util.List;

/*
    This class defines the adapter and interface for the term recyclerview.
 */

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

    private List<Term> terms = new ArrayList<>();
    private OnTermClickListener listener;

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        Term currentTerm = terms.get(position);
        holder.textViewTitle.setText(currentTerm.getTitle());
        holder.textViewDateStart.setText(DateFormatter.formatDate(currentTerm.getDateStart()));
        holder.textViewDateEnd.setText(DateFormatter.formatDate(currentTerm.getDateEnd()));
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        notifyDataSetChanged();
    }

    public Term getTermAt(int position) {
        return terms.get(position);
    }

    public Object[] getAllTerms() {
        return terms.toArray();
    }

    // This class associates the views with the correct UI elements.
    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        private TextView textViewDateStart;

        private TextView textViewDateEnd;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDateStart = itemView.findViewById(R.id.text_view_date_start);
            textViewDateEnd = itemView.findViewById(R.id.text_view_date_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onTermClick(terms.get(position));
                    }
                }
            });
        }
    }

    public interface OnTermClickListener {
        void onTermClick(Term term);
    }

    public void setOnTermClickListener(OnTermClickListener listener) {
        this.listener = listener;
    }

}
