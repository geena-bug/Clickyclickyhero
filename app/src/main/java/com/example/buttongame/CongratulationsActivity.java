package com.example.buttongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Set;

public class CongratulationsActivity extends AppCompatActivity {
    SharedPreferences pref;
    TextView title;
    TextView body;
    TextView score;
    Button quit;
    Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        title = findViewById(R.id.end_game_title);
        body = findViewById(R.id.body);
        score = findViewById(R.id.score);
        quit = findViewById(R.id.quit_btn);
        restart = findViewById(R.id.restart_btn);

        int totalCompleted = 0;

        pref = getApplicationContext().getSharedPreferences("ComboButtons", 0);
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);
        int totalScore = pref.getInt("totalScore", 0);

        if(completedTaskSet != null){
            totalCompleted = completedTaskSet.size();
            Log.d("Completed", String.valueOf(totalCompleted));
        }

        double percentageCompleted = totalCompleted > 0 ? ((double) totalCompleted / 5) * 100 : 0.0;
        String gameOver = getString(R.string.end_game_over);
        String gameOverText = getString(R.string.end_game_over_text, totalCompleted, 5);
        String congrats = getString(R.string.end_game_congrats);
        String congratsText = getString(R.string.end_game_congrats_text, totalCompleted, 5);

        title.setText(percentageCompleted > 80 ? congrats :  gameOver);
        //title.setTextColor(percentageCompleted > 80 ? getTextColor(R.color.green) : getTextColor(R.color.red));
        body.setText(percentageCompleted > 80 ? congratsText :  gameOverText);
        //body.setText(percentageCompleted > 80 ? getTextColor(R.color.green) : getTextColor(R.color.red));
        score.setText(String.valueOf(totalScore));

        quit.setOnClickListener(v -> {
            pref.edit().clear().apply();
            this.finish();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(1);
        });

        restart.setOnClickListener(v -> {
            pref.edit().clear().apply();
            finish();
            startActivity(new Intent(this, Home.class));
        });

    }

    int getTextColor(int color){
        return ContextCompat.getColor(this,color);
    }
}