package com.example.mathquizpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mathquizpro.databinding.ActivityMainBinding;
import com.example.mathquizpro.room.AppDataBase;
import com.example.mathquizpro.room.Attempt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // creating variables which holds answers right and wrong, position of them
    private ActivityMainBinding binding;
    private int rightAnswer, rightAnswerPosition, countOfQuestions = 0, countOfRightAnswers = 0;
    private boolean gameOver = false;
    private final int min = 5, max = 30;
    private final ArrayList<Integer> wrongAnswers = new ArrayList<>();


    // when program opens, onCreate method works, we use main functions inside it.
    // functions located inside onCreate always works while screen is still open
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupGame();
        startTimer();
    }
    // function hold 2 function: checkAnswer() and playNext()
    // so this function is always used until game is over
    private void setupGame() {
        //actions when button is clicked
        binding.textView0.setOnClickListener(v -> checkAnswer(binding.textView0.getText().toString()));
        binding.textView1.setOnClickListener(v -> checkAnswer(binding.textView1.getText().toString()));
        binding.textView2.setOnClickListener(v -> checkAnswer(binding.textView2.getText().toString()));
        binding.textView3.setOnClickListener(v -> checkAnswer(binding.textView3.getText().toString()));
        playNext();
    }



    // game is limited (60 seconds) when time is up it will go to send screen automatically
    private void startTimer() {
        // we use CountDownTimer from package android.os to count time backside
        new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.textViewTimer.setText(formatTime(millisUntilFinished));
                // if time is less then 10 second left textView that shows timer will change color to red
                if (millisUntilFinished <= 10000) {
                    binding.textViewTimer.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
                }
            }

            @Override
            public void onFinish() {
                // when time is up onFinish method works, first save scores, gameOver variable's value change
                saveHighScore();
                saveAttemptToDatabase();
                gameOver = true;
                // we use intent to send data to another screen key
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("result", countOfRightAnswers);
                intent.putExtra("countOfQuestions", countOfQuestions);
                startActivity(intent);
            }
        }.start();
    }

    private void saveAttemptToDatabase() {
        String timeText = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.ENGLISH)
                .format(System.currentTimeMillis());
        Attempt attempt = new Attempt(
                countOfRightAnswers,
                countOfQuestions, timeText
        );

        // Get the database instance and insert the attempt
        AppDataBase db = AppDataBase.getInstance(getApplicationContext());
        db.dao().insertAttempt(attempt);
    }


    // in this function has to method to generate questions and set on all views
    private void playNext() {
        generateQuestion();
        ArrayList<Integer> options = new ArrayList<>(wrongAnswers);
        options.add(rightAnswerPosition, rightAnswer);
        binding.textView0.setText(String.valueOf(options.get(0)));
        binding.textView1.setText(String.valueOf(options.get(1)));
        binding.textView2.setText(String.valueOf(options.get(2)));
        binding.textView3.setText(String.valueOf(options.get(3)));
        binding.textViewScore.setText(String.format(Locale.getDefault(), "%d/%d", countOfRightAnswers, countOfQuestions));
    }

    // generation questions
    private void generateQuestion() {
        Random random = new Random();
        // creating numbers 5 to 30
        int a = random.nextInt(max - min + 1) + min;
        int b = random.nextInt(max - min + 1) + min;

        boolean isPositive = random.nextBoolean();
        // calculation right answer by boolean
        rightAnswer = isPositive ? a + b : a - b;
        //String format
        binding.textViewQuestion.setText(String.format(Locale.getDefault(), "%d %s %d", a, isPositive ? "+" : "-", b));
        rightAnswerPosition = random.nextInt(4);
        wrongAnswers.clear();
        // creating wrong answers
        while (wrongAnswers.size() < 3) {
            int wrong = random.nextInt(max * 2 + 1) - (max - min);
            if (wrong != rightAnswer && !wrongAnswers.contains(wrong)) {
                wrongAnswers.add(wrong);
            }
        }
    }

    //checking answer
    private void checkAnswer(String selected) {
        // answer on button is string , change it to int and compare to right answer
        //if correct ++ right answers
        // and ++ quantity of questions then continue
        if (!gameOver) {
            if (Integer.parseInt(selected) == rightAnswer) {
                countOfRightAnswers++;
            }
            countOfQuestions++;
            playNext();
        }
    }

    // using SharedPreferences clas to save small data with key
    //compare max score before if greater then put new value into max
    private void saveHighScore() {
        SharedPreferences preferences = getSharedPreferences("math_game", MODE_PRIVATE);
        int maxScore = preferences.getInt("max", 0);
        if (countOfRightAnswers > maxScore) {
            preferences.edit().putInt("max", countOfRightAnswers).apply();
        }
    }

    // write mill second into minutes and seconds
    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        return String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
    }
}