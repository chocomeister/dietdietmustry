package com.fyp.dietandnutritionapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DietPreferenceFragment extends Fragment{
    ListView DietPreferenceListView;
    ArrayList<String> items;
    DietPreferenceAdapter adapter;
    ArrayList<DietPreference> dietpreference = new ArrayList<>();
    ArrayList<DietPreference> originaldietpreference = new ArrayList<>(); // Keep the unfiltered original list
    private EditText searchDietPreferenceEditText;

    private String savedSearchText = "";
    private int savedFilterPosition = 0;
    private int savedScrollPosition = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DietPreferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment viewAccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DietPreferenceFragment newInstance(String param1, String param2) {
        DietPreferenceFragment fragment = new DietPreferenceFragment ();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.viewalldietpreference, container, false);
        DietPreferenceListView = view.findViewById(R.id.dietPreferenceListView);

        // Set the adapter to the ListView
        //FAQAdapter adapter = new FAQAdapter(getContext(), faqs);
        adapter = new DietPreferenceAdapter(getContext(), dietpreference);
        DietPreferenceListView.setAdapter(adapter);

        // Scroll to the saved scroll position
        DietPreferenceListView.post(() -> DietPreferenceListView.setSelection(savedScrollPosition));

        DietPreferenceController dietPreferenceController = new DietPreferenceController(); // Ensure you have a member variable for ViewFAQController
        dietPreferenceController.getAllDietPreference(new DietPreferenceEntity.DataCallback() {
            @Override
            public void onSuccess(ArrayList<DietPreference> dietPreferences) {
                originaldietpreference.clear(); // Clear the original profiles list
                originaldietpreference.addAll(dietPreferences); // Store fetched profiles in the original list
                dietpreference.clear(); // Clear the filtering list
                dietpreference.addAll(dietPreferences); // Store profiles for filtering

                adapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load Diet Preference.", Toast.LENGTH_SHORT).show();
            }
        });


        DietPreferenceListView.setOnItemClickListener((parent, view1, position, id) -> {

            savedScrollPosition = DietPreferenceListView.getFirstVisiblePosition();

        });

        DietPreferenceListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            // Retrieve the specialization to delete
            DietPreference selectedDietPreference = dietpreference.get(position);

            // Show a confirmation dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Diet Preference")
                    .setMessage("Are you sure you want to delete this diet preference?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call the delete function if the user confirms
                        deleteDietPreferenceFromDatabase(selectedDietPreference);

                        // Remove the specialization from the list and notify the adapter
                        dietpreference.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

        return view;
    }

    private void deleteDietPreferenceFromDatabase(DietPreference dietPreference) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Deleting diet preference...");
        pd.show();

        // Get the ID of the specialization to delete
        String dietPreferenceId = dietPreference.getDietPreferenceId();

        DietPreferenceController controller = new DietPreferenceController();
        controller.deleteDietPreference(dietPreferenceId, new DietPreferenceEntity.DeleteCallback() {
            @Override
            public void onSuccess() {
                pd.dismiss();
                Toast.makeText(getContext(), "Diet Preference deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), "Failed to delete specialization: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
