package com.example.dietandnutritionapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.List;

public class ConsultationsUFragment extends Fragment {

    private ListView nutritionistListView;
    private List<Nutritionist> nutritionistList;
/*
    private UserConsultationsController adapter;
*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultations_u, container, false);

        // Initialize the ListView
        nutritionistListView = view.findViewById(R.id.nutritionist_list_view);

        // Initialize the list of nutritionists
        nutritionistList = new ArrayList<>();
        // Add dummy data (replace with real data source)
        nutritionistList.add(new Nutritionist("jane.doe@example.com", "Dr. Jane Doe", "PhD in Nutrition", "123-456-7890", "Weight Loss", "Experienced nutritionist specializing in weight loss.", null));
        nutritionistList.add(new Nutritionist("john.doe@example.com", "Dr. John Doe", "MSc in Dietetics", "987-654-3210", "Sports Nutrition", "Expert in sports nutrition and diet planning.", null));

        // Set up the adapter

        /*adapter = new UserConsultationsController(getActivity(), nutritionistList);
        nutritionistListView.setAdapter(adapter);*/

        // Handle item clicks
        nutritionistListView.setOnItemClickListener((parent, view1, position, id) -> {
            Nutritionist selectedNutritionist = nutritionistList.get(position);
            //Intent intent = new Intent(getActivity(), NutritionistProfileActivity.class);
            //intent.putExtra("nutritionist_email", selectedNutritionist.getUsername()); // Pass data to the profile activity
            //startActivity(intent);
        });

        return view;
    }
}
