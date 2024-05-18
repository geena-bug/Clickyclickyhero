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
/**
 * The main activity of the clicky clicky hero app, which displays a list of combo challenges.
 * Users can attempt these combos, and their progress is tracked via SharedPreferences.
 */
public class Home extends AppCompatActivity {
    // List of combo challenges.
    ArrayList<Combo> comboArrayList;
    RecyclerView comboList; // RecyclerView for displaying combos.
    ComboAdapter comboAdapter;  // Adapter for the RecyclerView.

    // Drawable resource IDs for arrows used in combos.
    int upArrow;
    int downArrow;
    int leftArrow;
    int rightArrow;

    ArrayList<String> completedCombos;
    ArrayList<String> failedCombo;     // Lists to keep track of completed and failed combos.

    String counterComboText = "Correct Combos: ";

// SharedPreferences to store user progress.
    /**
     * Called when the activity is starting. Initializes the UI and sets up data handling.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SharedPreferences for storing and retrieving game progress.

        pref = getApplicationContext().getSharedPreferences("ComboButtons", 0);
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);

        TextView comboCounter = findViewById(R.id.comboCounter);
// Update the UI with the number of completed combos.
        if(completedTaskSet != null){
            completedCombos = new ArrayList<String>(completedTaskSet);
            comboCounter.setText(getString(R.string.correct_combos_var, completedCombos.size()));
        }
        // Initialize drawable IDs.
        upArrow = R.drawable.up_arrow;
        downArrow =R.drawable.down_arrow;
        leftArrow =R.drawable.left_arrow;
        rightArrow =R.drawable.right_arrow;

        // Setup RecyclerView.
        comboList = findViewById(R.id.rvComboList);
        comboList.setLayoutManager(new LinearLayoutManager(Home.this));

        // Populate the list of combos and shuffle them for variety.
        comboArrayList = new ArrayList<>();
        comboArrayList.add(new Combo(1, upArrow, downArrow,rightArrow,leftArrow,upArrow, "Reinforce" ));
        comboArrayList.add(new Combo(2, downArrow, downArrow,upArrow, rightArrow, 0,  "Resupply"));
        comboArrayList.add(new Combo(3, upArrow, upArrow,leftArrow, upArrow, rightArrow,  "Eagle Rearm"));
        comboArrayList.add(new Combo(4, upArrow, rightArrow,downArrow, rightArrow, 0,  "Eagle Airstrike"));
        comboArrayList.add(new Combo(5, upArrow, leftArrow,downArrow, downArrow, downArrow,  "Eagle 500Kg Bomb"));

        Collections.shuffle(comboArrayList);

        // Create and set the adapter for RecyclerView.
        comboAdapter = new ComboAdapter(comboArrayList);
        comboList.setAdapter(comboAdapter);

    }
    /**
     * Initializes the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.restart){
            // Clear all SharedPreferences data and restart the activity.
            pref.edit().clear().apply();
            finish();
            startActivity(getIntent());
        }
        return true;
    }
}