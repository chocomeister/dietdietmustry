package com.fyp.dietandnutritionapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ConsultationsFragmentAdapter_u_consult extends BaseAdapter {

    private Context context;
    private ArrayList<Consultation> consultationSlots;
    private LayoutInflater inflater;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public ConsultationsFragmentAdapter_u_consult(Context context, ArrayList<Consultation> consultationSlots) {
        super();
        this.context = context;
        this.consultationSlots = consultationSlots;
        this.inflater = LayoutInflater.from(context);
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return consultationSlots.size();
    }

    @Override
    public Object getItem(int position) {
        return consultationSlots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Reuse the convertView if possible
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.consultation_item, parent, false);
            viewHolder = new ViewHolder();
            // Initialize the views
            viewHolder.consultationIdTextView = convertView.findViewById(R.id.consultation_id_value);
            viewHolder.nutritionistNameTextView = convertView.findViewById(R.id.nutritionist_name);
            viewHolder.dateTextView = convertView.findViewById(R.id.date);
            viewHolder.timeTextView = convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the ConsultationSlot object for the current position
        Consultation currentSlot = (Consultation) getItem(position);

        // Check for null values and set data to the views
        if (currentSlot != null) {
            viewHolder.consultationIdTextView.setText(currentSlot.getConsultationId());
            viewHolder.nutritionistNameTextView.setText(currentSlot.getNutritionistName());
            viewHolder.dateTextView.setText(currentSlot.getDate());
            viewHolder.timeTextView.setText(currentSlot.getTime());
        } else {
            Toast.makeText(context, "Consultation slot data is not available.", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }

    // Method to update the consultation list and notify the adapter
    public void setConsultationSlots(ArrayList<Consultation> slots) {
        this.consultationSlots.clear(); // Clear the old data
        this.consultationSlots.addAll(slots); // Add the new data
        notifyDataSetChanged(); // Notify that the data has changed
    }

    static class ViewHolder {
        TextView consultationIdTextView;
        TextView nutritionistNameTextView;
        TextView dateTextView;
        TextView timeTextView;
    }
}