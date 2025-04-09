package com.example.mathquizpro.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AttemptDao {

    @Insert
    void insertAttempt(Attempt attempt);

    @Query("SELECT * FROM quiz_attempts ORDER BY correctAnswers DESC")
    List<Attempt> getAllAttempts();

    @Delete
    void deleteAttempt(Attempt attempt);

}
