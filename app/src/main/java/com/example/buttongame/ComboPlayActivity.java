package com.example.buttongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buttongame.data.Combo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The activity class responsible for handling the gameplay of a combo challenge.
 * It displays a sequence of arrow images and responds to user inputs by comparing them to the expected sequence.
 */
public class ComboPlayActivity extends AppCompatActivity {

    private ImageView img1, img2, img3, img4, img5;
    private int upArrow, downArrow, leftArrow, rightArrow;
    private int starGold, starGrey;

    private ImageButton topBtn, downBtn, leftBtn, rightBtn;
    private TextView tvTitle;

    private int totalScore, weight = 5, maximumScore = 0;
    private int totalAvailableClicks = 4;
    private int clickCounter = 1;

    private Combo combo;
    private ArrayList<Integer> imagesResource;
    private Set<String> completedCombos, failedCombos;

    private Button contBtn;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_play);

        // Initialize array to hold image resources
        imagesResource = new ArrayList<>();

        // Retrieve Combo object passed from previous activity
        Intent intent = getIntent();
        combo = (Combo) intent.getSerializableExtra("Combo");

        // Add images from the combo to the resource list
        imagesResource.add(combo.getImage1());
        imagesResource.add(combo.getImage2());
        imagesResource.add(combo.getImage3());
        imagesResource.add(combo.getImage4());
        imagesResource.add(combo.getImage5());
        // Initialize resource IDs for arrows and stars
        upArrow = R.drawable.up_arrow;
        downArrow = R.drawable.down_arrow;
        leftArrow = R.drawable.left_arrow;
        rightArrow = R.drawable.right_arrow;
        starGold = android.R.drawable.btn_star_big_on;
        starGrey = android.R.drawable.btn_star_big_off;

        // Set up UI elements
        tvTitle = findViewById(R.id.combo_title);
        tvTitle.setText(combo.getTitle());

        img1 = findViewById(R.id.img1);
        img1.setImageResource(combo.getImage1());
        img2 = findViewById(R.id.img2);
        img2.setImageResource(combo.getImage2());
        img3 = findViewById(R.id.img3);
        img3.setImageResource(combo.getImage3());
        img4 = findViewById(R.id.img4);
        img4.setImageResource(combo.getImage4());
        img5 = findViewById(R.id.img5);
        if (combo.getImage5() > 0) {
            img5.setImageResource(combo.getImage5());
            totalAvailableClicks++;
            maximumScore += weight;
        }

        // Calculate the maximum possible score
        maximumScore = weight * totalAvailableClicks;

        // Setup button handlers
        buttonHandler();
    }

    /**
     * Initializes and sets onClick listeners for all interactive buttons on the screen.
     */
    private void buttonHandler() {
        topBtn = findViewById(R.id.topBtn);
        downBtn = findViewById(R.id.downBtn);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        contBtn = findViewById(R.id.cont_btn);

        // Register onClickListeners to handle arrow button presses
        topBtn.setOnClickListener(v -> setStar(upArrow));
        downBtn.setOnClickListener(v -> setStar(downArrow));
        leftBtn.setOnClickListener(v -> setStar(leftArrow));
        rightBtn.setOnClickListener(v -> setStar(rightArrow));
        contBtn.setOnClickListener(v -> save());
    }

    /**
     * Handles the logic of checking if the button pressed matches the expected sequence
     * and updates the UI accordingly.
     * @param compareImg2 The image resource id of the button pressed.
     */
    private void setStar(int compareImg2) {
        if (totalAvailableClicks == clickCounter) {
            Log.d("clickCounter", totalAvailableClicks + " => " + clickCounter);
            contBtn.setVisibility(View.VISIBLE);
        }

        if (totalAvailableClicks >= clickCounter) {
            int compareImg1 = imagesResource.get(clickCounter - 1);
            int star = starGrey;
            if (compareImg1 == compareImg2) {
                star = starGold;
                totalScore += weight;
            }

            ImageView[] images = {img1, img2, img3, img4, img5};
            if (clickCounter <= images.length) {
                images[clickCounter - 1].setImageResource(star);
            }

            clickCounter++;
        }
    }

    /**
     * Saves the outcome of the game to SharedPreferences and decides the next screen to navigate based on the attempt count.
     */
    private void save() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ComboButtons", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int currentTotalScore = pref.getInt("totalScore", 0);
        completedCombos = pref.getStringSet("completedCombos", new HashSet<>());
        failedCombos = pref.getStringSet("failedCombos", new HashSet<>());

        int attemptCount = completedCombos.size() + failedCombos.size();

        if (totalScore == maximumScore) {
            completedCombos.add(String.valueOf(combo.getId()));
        } else {
            failedCombos.add(String.valueOf(combo.getId()));
        }

        editor.putInt("totalScore", currentTotalScore + totalScore);
        editor.putStringSet("completedCombos", completedCombos);
        editor.putStringSet("failedCombos", failedCombos);
        editor.apply();

        Toast.makeText(this, "Completed task", Toast.LENGTH_LONG).show();

        if (attemptCount < 4) {
            goToMainActivity();
        } else {
            goToCongratulationsActivity();
        }
    }

    /**
     * Navigates back to the main activity of the application.
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    /**
     * Navigates to a congratulations screen upon successful completion of tasks.
     */
    private void goToCongratulationsActivity() {
        Intent intent = new Intent(this, CongratulationsActivity.class);
        startActivity(intent);
    }
}
