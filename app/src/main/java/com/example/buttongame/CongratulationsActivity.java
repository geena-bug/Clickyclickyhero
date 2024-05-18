package com.example.buttongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Set;

/**
 * This activity is shown to users after they have completed a set of challenges in the ButtonGame.
 * It presents the final score and offers options to restart the game or exit.
 */
public class CongratulationsActivity extends AppCompatActivity {
    SharedPreferences pref;
    TextView title;
    TextView body;
    TextView score;
    Button quit;
    Button restart;

    /**
     * Called when the activity is starting. This method initializes the UI components and sets up event listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        // Initialization of UI components
        title = findViewById(R.id.end_game_title);
        body = findViewById(R.id.body);
        score = findViewById(R.id.score);
        quit = findViewById(R.id.quit_btn);
        restart = findViewById(R.id.restart_btn);

        // Retrieve completion and score data from SharedPreferences
        pref = getApplicationContext().getSharedPreferences("ComboButtons", 0);
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);
        int totalScore = pref.getInt("totalScore", 0);


        int totalCompleted = completedTaskSet != null ? completedTaskSet.size() : 0;
        Log.d("Completed", String.valueOf(totalCompleted));

        // Calculate the percentage of tasks completed
        double percentageCompleted = totalCompleted > 0 ? ((double) totalCompleted / 5) * 100 : 0.0;
        String gameOver = getString(R.string.end_game_over);
        String gameOverText = getString(R.string.end_game_over_text, totalCompleted, 5);
        String congrats = getString(R.string.end_game_congrats);
        String congratsText = getString(R.string.end_game_congrats_text, totalCompleted, 5);

        // Set text and color based on the percentage completed
        title.setText(percentageCompleted > 80 ? congrats :  gameOver);
        body.setText(percentageCompleted > 80 ? congratsText :  gameOverText);
        score.setText(String.valueOf(totalScore));

        // Set click listener for the quit button
        quit.setOnClickListener(v -> {
            // Clears all shared preferences, finishes the activity, kills the process, and exits the app
            pref.edit().clear().apply();
            this.finish();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(1);
        });

        // Set click listener for the restart button
        restart.setOnClickListener(v -> {
            // Clears all shared preferences, finishes this activity, and starts the Home activity anew
            pref.edit().clear().apply();
            finish();
            startActivity(new Intent(this, Home.class));
        });
    }

    /**
     * Retrieves the color resource associated with a particular color ID.
     * @param color The resource ID of the color.
     * @return The resolved color value.
     */
    int getTextColor(int color) {
        return ContextCompat.getColor(this, color);
    }
}
