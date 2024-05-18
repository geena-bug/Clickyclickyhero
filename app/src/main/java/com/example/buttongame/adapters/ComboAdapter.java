package com.example.buttongame.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buttongame.ComboPlayActivity;
import com.example.buttongame.R;
import com.example.buttongame.data.Combo;

import java.util.ArrayList;
import java.util.Set;

/**
 * Adapter for managing a collection of Combo items in a RecyclerView.
 * This adapter handles the creation of view holders, binding data to views,
 * and setting up click listeners for each item to navigate to a gameplay activity.
 */
public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {

    // List of all combos to be displayed.
    private final ArrayList<Combo> comboArrayList;

    // Context in which the adapter is operating, usually the parent Activity or Fragment.
    private Context context;

    // Lists to track completed and failed combos.
    ArrayList<String> completedCombos;
    ArrayList<String> failedCombo;

    /**
     * Constructor initializing the adapter with a list of combos.
     * Logs the size of the combo list for debugging purposes.
     *
     * @param comboArrayList List of combos to be displayed.
     */
    public ComboAdapter(ArrayList<Combo> comboArrayList){
        Log.d("ComboList Size","Combo list => " + comboArrayList.size());
        this.comboArrayList = comboArrayList;
    }

    /**
     * Inflates the row layout from XML and returns the holder.
     *
     * @param parent   ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ComboAdapter.ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrows_combo, parent, false);
        this.context = parent.getContext();
        return new ComboViewHolder(view);
    }

    /**
     * Binds data to the view holder at the specified position.
     * This method also sets up the logic to handle clicks and navigates to the gameplay activity.
     *
     * @param holder   The view holder whose state should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ComboAdapter.ComboViewHolder holder, int position) {
        boolean nextActivity = true;
        SharedPreferences pref = this.context.getSharedPreferences("ComboButtons", Context.MODE_PRIVATE);
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);
        Set<String> failedTaskSet = pref.getStringSet("failedCombos", null);

        if(completedTaskSet != null){
            completedCombos = new ArrayList<>(completedTaskSet);
        }

        if(failedTaskSet != null){
            failedCombo = new ArrayList<>(failedTaskSet);
        }

        holder.tvTitle.setText(comboArrayList.get(position).getTitle());
        holder.img1.setImageResource(comboArrayList.get(position).getImage1());
        holder.img2.setImageResource(comboArrayList.get(position).getImage2());
        holder.img3.setImageResource(comboArrayList.get(position).getImage3());
        holder.img4.setImageResource(comboArrayList.get(position).getImage4());

        int img5 = comboArrayList.get(position).getImage5();
        if(img5 > 0) {
            holder.img5.setImageResource(img5);
        }

        if(completedCombos != null && completedCombos.contains(String.valueOf(comboArrayList.get(position).getId()))){
            holder.itemView.setBackgroundResource(R.color.green);
            nextActivity = false;
        }

        if(failedCombo != null && failedCombo.contains(String.valueOf(comboArrayList.get(position).getId()))){
            holder.itemView.setBackgroundResource(R.color.red);
            nextActivity = false;
        }

        final boolean finalNextActivity = nextActivity;
        holder.itemView.setOnClickListener(v -> {
            if(!finalNextActivity){
                Toast.makeText(this.context, "Task already completed", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(this.context, ComboPlayActivity.class);
                intent.putExtra("Combo", comboArrayList.get(position));
                this.context.startActivity(intent);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.comboArrayList.size();
    }

    /**
     * ViewHolder class for layout elements. This class holds all the UI elements that will be modified
     * based on the data object it represents in the RecyclerView.
     */
    public static class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout linearLayout;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder method.
         *
         * @param itemView The root view of the list item layout.
         */
        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.combo_title);
            linearLayout = itemView.findViewById(R.id.main_layout);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            img4 = itemView.findViewById(R.id.img4);
            img5 = itemView.findViewById(R.id.img5);
        }
    }
}
