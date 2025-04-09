package com.example.mathquizpro.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_attempts")
public class Attempt {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int correctAnswers;
    public int totalQuestions;
    public String timeText;

    public Attempt(int correctAnswers, int totalQuestions,String timeText) {
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.timeText = timeText;
    }
}
