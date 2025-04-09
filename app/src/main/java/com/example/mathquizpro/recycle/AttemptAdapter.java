package com.example.mathquizpro.recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathquizpro.R;
import com.example.mathquizpro.room.Attempt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.AttemptViewHolder> {
    private final List<Attempt> attempts;

    public AttemptAdapter(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    @NonNull
    @Override
    public AttemptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attempt,parent,false);
        return new AttemptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptViewHolder holder, int position) {
Attempt attempt = attempts.get(position);
        holder.textViewCorrectAnswers.setText("Correct answers: " + attempt.correctAnswers);
        holder.textViewTotalQuestions.setText("Total questions: " + attempt.totalQuestions);
        holder.textViewTime.setText(attempt.timeText);
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    static class AttemptViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCorrectAnswers, textViewTotalQuestions, textViewTime;

        AttemptViewHolder(View itemView) {
            super(itemView);
            textViewCorrectAnswers = itemView.findViewById(R.id.textViewCorrectAnswers);
            textViewTotalQuestions = itemView.findViewById(R.id.textViewTotalQuestions);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
