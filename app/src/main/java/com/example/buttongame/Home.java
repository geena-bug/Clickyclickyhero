package com.example.buttongame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buttongame.adapters.ComboAdapter;
import com.example.buttongame.data.Combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Home extends AppCompatActivity {

    ArrayList<Combo> comboArrayList;
    RecyclerView comboList;
    ComboAdapter comboAdapter;
    int upArrow;
    int downArrow;
    int leftArrow;
    int rightArrow;

    ArrayList<String> completedCombos;
    ArrayList<String> failedCombo;

    String counterComboText = "Correct Combos: ";

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = getApplicationContext().getSharedPreferences("ComboButtons", 0);
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);

        TextView comboCounter = findViewById(R.id.comboCounter);

        if(completedTaskSet != null){
            completedCombos = new ArrayList<String>(completedTaskSet);
            comboCounter.setText(getString(R.string.correct_combos_var, completedCombos.size()));
        }

        upArrow = R.drawable.up_arrow;
        downArrow =R.drawable.down_arrow;
        leftArrow =R.drawable.left_arrow;
        rightArrow =R.drawable.right_arrow;
        comboList = findViewById(R.id.rvComboList);
        comboList.setLayoutManager(new LinearLayoutManager(Home.this));

        comboArrayList = new ArrayList<>();
        comboArrayList.add(new Combo(1, upArrow, downArrow,rightArrow,leftArrow,upArrow, "Reinforce" ));
        comboArrayList.add(new Combo(2, downArrow, downArrow,upArrow, rightArrow, 0,  "Resupply"));
        comboArrayList.add(new Combo(3, upArrow, upArrow,leftArrow, upArrow, rightArrow,  "Eagle Rearm"));
        comboArrayList.add(new Combo(4, upArrow, rightArrow,downArrow, rightArrow, 0,  "Eagle Airstrike"));
        comboArrayList.add(new Combo(5, upArrow, leftArrow,downArrow, downArrow, downArrow,  "Eagle 500Kg Bomb"));

        Collections.shuffle(comboArrayList);

        comboAdapter = new ComboAdapter(comboArrayList);
        comboList.setAdapter(comboAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.restart){
            pref.edit().clear().apply();
            finish();
            startActivity(getIntent());
        }
        return true;
    }
}