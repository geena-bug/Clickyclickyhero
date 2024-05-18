package com.example.buttongame.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {

    private final ArrayList<Combo> comboArrayList;

    private Combo selectedCombo;

    private Context context;

    ArrayList<String> completedCombos;
    ArrayList<String> failedCombo;

    int compledIndx;

    int failedIndx;

    public ComboAdapter(ArrayList<Combo> comboArrayList){
        Log.d("ComboList Size","Combo list => " + comboArrayList.size());
        this.comboArrayList = comboArrayList;
    }

    /**
     * The code below loads the layout and makes all the element on the layout accessible  via the view holder
     */
    @NonNull
    @Override
    public ComboAdapter.ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrows_combo, parent, false);
        this.context = parent.getContext();
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboAdapter.ComboViewHolder holder, int position) {
        boolean nextActivity = true;
        SharedPreferences pref = this.context.getSharedPreferences("ComboButtons", 0); // 0 - for private mode
        Set<String> completedTaskSet = pref.getStringSet("completedCombos", null);
        Set<String> failedTaskSet = pref.getStringSet("failedCombos", null);
        if(completedTaskSet != null){
            completedCombos = new ArrayList<String>(completedTaskSet);
        }

        if(failedTaskSet != null){
            failedCombo = new ArrayList<String>(failedTaskSet);
        }

        holder.tvTitle.setText(comboArrayList.get(position).getTitle());
        holder.img1.setImageResource(comboArrayList.get(position).getImage1());
        holder.img2.setImageResource(comboArrayList.get(position).getImage2());
        holder.img3.setImageResource(comboArrayList.get(position).getImage3());
        holder.img4.setImageResource(comboArrayList.get(position).getImage4());

        int img5 = comboArrayList.get(position).getImage5();
        if(img5 > 0) {
            holder.img5.setImageResource(comboArrayList.get(position).getImage5());
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
                //here we are passing an instance of the Combo (with selected data) class to the next activity
                intent.putExtra("Combo",comboArrayList.get(position));
                this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.comboArrayList.size();
    }
    public Combo getSelectedCombo() {
        return selectedCombo;
    }
    public static  class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout linearLayout;
        ImageView  img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;
        //Access the elements on the layout
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

