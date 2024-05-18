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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buttongame.data.Combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ComboPlayActivity extends AppCompatActivity {

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

    int upArrow;
    int downArrow;
    int leftArrow;
    int rightArrow;

    int starGold;

    int starGrey;

    ImageButton topBtn;
    ImageButton downBtn;
    ImageButton leftBtn;
    ImageButton rightBtn;
    TextView tvTitle;

    int totalScore;
    int weight = 5;
    int maximumScore = 0;

    int totalAvailableClicks = 4;

    Combo combo;

    int clickCounter = 1;

    ArrayList<Integer> imagesResource;
    Set<String> completedCombos;
    Set<String> failedCombos;

    Button contBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_play);

        imagesResource = new ArrayList<>();

        //receive intent data
        Intent intent = getIntent();
        combo = (Combo) intent.getSerializableExtra("Combo");


        //add images
        imagesResource.add(combo.getImage1());
        imagesResource.add(combo.getImage2());
        imagesResource.add(combo.getImage3());
        imagesResource.add(combo.getImage4());
        imagesResource.add(combo.getImage5());

        upArrow = R.drawable.up_arrow;
        downArrow = R.drawable.down_arrow;
        leftArrow = R.drawable.left_arrow;
        rightArrow = R.drawable.right_arrow;

        starGold = android.R.drawable.btn_star_big_on;
        starGrey = android.R.drawable.btn_star_big_off;

        tvTitle = findViewById(R.id.combo_title);
        tvTitle.setText(combo.getTitle());

        //set image views
        img1 = findViewById(R.id.img1);
        img1.setImageResource(combo.getImage1());

        img2 = findViewById(R.id.img2);
        img2.setImageResource(combo.getImage2());

        img3 = findViewById(R.id.img3);
        img3.setImageResource(combo.getImage3());

        img4 = findViewById(R.id.img4);
        img4.setImageResource(combo.getImage4());

        maximumScore = weight * totalAvailableClicks;

        img5 = findViewById(R.id.img5);
        if(combo.getImage5() > 0){
            totalAvailableClicks++;
            maximumScore+= weight;
            img5.setImageResource(combo.getImage5());
        }
        //handle button clicks
        buttonHandler();
    }

    private void buttonHandler(){
        topBtn = findViewById(R.id.topBtn);
        downBtn = findViewById(R.id.downBtn);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        contBtn = findViewById(R.id.cont_btn);

        topBtn.setOnClickListener(v -> {
            setStar(upArrow);
        });

        downBtn.setOnClickListener(v -> {
            setStar(downArrow);
        });

        leftBtn.setOnClickListener(v -> {
            setStar(leftArrow);
        });

        rightBtn.setOnClickListener(v -> {
            setStar(rightArrow);
        });

        contBtn.setOnClickListener(v -> {
            save();
        });
    }

   void setStar(int compareImg2){

       if(totalAvailableClicks == clickCounter) {
           Log.d("clickCounter",String.valueOf(totalAvailableClicks) + " => " + clickCounter);
           contBtn.setVisibility(View.VISIBLE);
       }

       if(totalAvailableClicks >= clickCounter){

           int compareImg1 = imagesResource.get(clickCounter - 1);
           int star = starGrey;

           if(compareImg1 == compareImg2){
               star = starGold;
               totalScore+=weight;
           }

           switch (clickCounter){
               case 1:
                   img1.setImageResource(star);
                   break;
               case 2:
                   img2.setImageResource(star);
                   break;
               case 3:
                   img3.setImageResource(star);
                   break;

               case 4:
                   img4.setImageResource(star);
                   break;

               case 5:
                   img5.setImageResource(star);
                   break;
           }

           clickCounter++;
       }

    }

    void save(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ComboButtons", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        int currentTotalScore = pref.getInt("totalScore", 0);
        int attemptCount = 0;
        completedCombos = pref.getStringSet("completedCombos", null);
        failedCombos = pref.getStringSet("failedCombos", null);

        if(completedCombos == null){
            completedCombos = new HashSet<>();
        }else{
            attemptCount += completedCombos.size();
        }

        if(failedCombos == null){
            failedCombos = new HashSet<>();
        }else{
            attemptCount += failedCombos.size();
        }

        if(totalScore == maximumScore){
            completedCombos.add(String.valueOf(combo.getId()));
        }else{
            failedCombos.add(String.valueOf(combo.getId()));
        }

        editor.putInt("totalScore", currentTotalScore + totalScore);
        editor.putStringSet("completedCombos", completedCombos);
        editor.putStringSet("failedCombos", failedCombos);
        editor.apply();

        Toast.makeText(this, "Completed task", Toast.LENGTH_LONG).show();

        if(attemptCount < 4){
            goToMainActivity();
        }else{
            goToCongratulationsActivity();
        }
    }

    void goToMainActivity(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    void goToCongratulationsActivity(){
        Intent intent = new Intent(this, CongratulationsActivity.class);
        startActivity(intent);
    }
}