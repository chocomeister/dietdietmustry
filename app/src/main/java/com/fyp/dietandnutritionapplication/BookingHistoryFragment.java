package com.fyp.dietandnutritionapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryFragment extends Fragment {

    private RecyclerView bookingConsultationsRecyclerView;
    private List<Consultation> consultationList;
    private BookingConsultationsAdapter adapter;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_consultations, container, false);

        // Initialize buttons
        Button button_booking_history = view.findViewById(R.id.booking_history);
        Button button_consultationSlot = view.findViewById(R.id.consultation_slot);
        Button button_pendingConsultation = view.findViewById(R.id.pending_consultation);

        // Initialize RecyclerView
        bookingConsultationsRecyclerView = view.findViewById(R.id.booking_consultation_recycler_view);
        bookingConsultationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        // Hardcoded data for the prototype
        consultationList = new ArrayList<>();
        adapter = new BookingConsultationsAdapter(consultationList);
        bookingConsultationsRecyclerView.setAdapter(adapter);

//        consultationList.add(new Consultation("6 Oct 2024 (Sun), 3:30 PM", "Ms. Tee Zhi Xi", "Confirmed"));
//        consultationList.add(new Consultation("20 Nov 2024 (Sun), 7:30 PM", "Mr. Goh Xiao Ming", "Confirmed"));
//        consultationList.add(new Consultation("17 Nov 2024 (Sun), 4:30 PM", "Mr. Alex", "Confirmed"));
//        consultationList.add(new Consultation("8 Dec 2024 (Sun), 10:30 AM", "Ms. Vivian", "Confirmed"));

        // Set up adapter
        BookingConsultationsAdapter adapter = new BookingConsultationsAdapter(consultationList);
        bookingConsultationsRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchConsultationSlots();

        button_booking_history.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new BookingHistoryFragment())
                    .addToBackStack(null)  // Add to back stack to enable back navigation
                    .commit();
        });

        button_consultationSlot.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ConsultationNFragment())
                    .addToBackStack(null)
                    .commit();
        });

        button_pendingConsultation.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new PendingConsultationsFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }

    // Fetch consultation slots from Firestore
    private void fetchConsultationSlots() {
        CollectionReference consultationsRef = db.collection("Consultation_slots");

        consultationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                consultationList.clear();  // Clear the list before adding new items
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get data from the document
                    String dateTime = document.getString("dateTime");
                    String clientName = document.getString("clientName");
                    String status = document.getString("status");

                    // Add the fetched consultation to the list
                    consultationList.add(new Consultation(dateTime, clientName, status));
                }
                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            } else {
                // Handle the error
                 Log.d("FirestoreError", "Error getting documents: ", task.getException());
            }
        });
    }

    // Consultation data model
    private static class Consultation {
        String dateTime;
        String clientName;
        String status;

        public Consultation(String dateTime, String clientName, String status) {
            this.dateTime = dateTime;
            this.clientName = clientName;
            this.status = status;
        }
    }

    // RecyclerView Adapter
    private static class BookingConsultationsAdapter extends RecyclerView.Adapter<BookingConsultationsAdapter.ConsultationViewHolder> {
        private final List<Consultation> consultations;

        public BookingConsultationsAdapter(List<Consultation> consultations) {
            this.consultations = consultations;
        }

        @NonNull
        @Override
        public ConsultationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false);
            return new ConsultationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ConsultationViewHolder holder, int position) {
            Consultation consultation = consultations.get(position);
            holder.dateTimeTextView.setText(consultation.dateTime);
            holder.clientNameTextView.setText(consultation.clientName);
            holder.statusTextView.setText(consultation.status);
        }

        @Override
        public int getItemCount() {
            return consultations.size();
        }

        static class ConsultationViewHolder extends RecyclerView.ViewHolder {
            TextView dateTimeTextView;
            TextView clientNameTextView;
            TextView statusTextView;

            public ConsultationViewHolder(@NonNull View itemView) {
                super(itemView);
                dateTimeTextView = itemView.findViewById(R.id.consultation_date_time);
                clientNameTextView = itemView.findViewById(R.id.consultation_client_name);
                statusTextView = itemView.findViewById(R.id.consultation_status);
            }
        }
    }
}
