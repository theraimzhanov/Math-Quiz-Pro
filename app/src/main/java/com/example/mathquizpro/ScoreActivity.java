package com.example.mathquizpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathquizpro.databinding.ActivityScoreBinding;
import com.example.mathquizpro.recycle.AttemptAdapter;
import com.example.mathquizpro.room.AppDataBase;
import com.example.mathquizpro.room.Attempt;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showCurrentResult();     // Show the result of this attempt
        showAllAttempts();

    }

    private void showCurrentResult() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("result") && intent.hasExtra("countOfQuestions")) {
            int result = intent.getIntExtra("result", 0);
            int count = intent.getIntExtra("countOfQuestions", 0);

            binding.textViewResult.setText("Your result: " + result);
            binding.textViewCountQuestion.setText("Questions: " + count);

            SharedPreferences preferences = getSharedPreferences("math_game", MODE_PRIVATE);
            int max = preferences.getInt("max", 0);
            binding.textViewRecord.setText("Your record: " + max);
        }
    }

    private void showAllAttempts() {
        AppDataBase db = AppDataBase.getInstance(getApplicationContext());
        List<Attempt> attempts = db.dao().getAllAttempts();

        AttemptAdapter adapter = new AttemptAdapter(attempts);
        RecyclerView recyclerView = binding.recyclerViewAttempts;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
int position = viewHolder.getAdapterPosition();
Attempt item = attempts.get(position);

db.dao().deleteAttempt(item);
attempts.remove(position);
adapter.notifyItemRemoved(position);
                Toast.makeText(ScoreActivity.this, "Successfully deleted!!!", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
 
    // function to go back and play again
    public void onClickNewGame(View view) {
        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}