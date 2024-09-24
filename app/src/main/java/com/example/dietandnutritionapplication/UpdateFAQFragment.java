package com.example.dietandnutritionapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateFAQFragment extends Fragment {

    private FAQ selectedFAQ;
    private TextView faqIdTextView;
    private EditText titleEditText;
    private EditText questionEditText;
    private EditText answerEditText;
    private EditText datecreatedEditText;
    private Button updateButton;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_faq, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Find EditText fields
        faqIdTextView = view.findViewById(R.id.faqId);
        titleEditText = view.findViewById(R.id.titleEdit);
        questionEditText = view.findViewById(R.id.questionEdit);
        answerEditText = view.findViewById(R.id.answerEdit);
        datecreatedEditText = view.findViewById(R.id.dateEdit);
        updateButton = view.findViewById(R.id.saveFAQ);  // Assume you have an update button in the layout

        faqIdTextView.setVisibility(View.GONE);  // Programmatically hides the TextView


        // Retrieve the selected FAQ from the arguments
        if (getArguments() != null) {
            selectedFAQ = (FAQ) getArguments().getSerializable("selectedFAQ");

            // Populate EditText fields with selectedFAQ details
            if (selectedFAQ != null) {
                faqIdTextView.setText(selectedFAQ.getFaqId());
                titleEditText.setText(selectedFAQ.getTitle());
                questionEditText.setText(selectedFAQ.getQuestion());
                answerEditText.setText(selectedFAQ.getAnswer());
                datecreatedEditText.setText(selectedFAQ.getDateCreated());
            }
        }

        // Set an onClickListener on the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to update FAQ in Firestore
//                updateFAQInFirestore();
                String updatedTitle = titleEditText.getText().toString();
                String updatedQuestion = questionEditText.getText().toString();
                String updatedAnswer = answerEditText.getText().toString();
                String updatedDate = datecreatedEditText.getText().toString();
                if (selectedFAQ != null && selectedFAQ.getFaqId() != null){
                    String id = selectedFAQ.getFaqId();
                    UpdateFAQController updateFAQController = new UpdateFAQController();
                    updateFAQController.chechUpdateFAQ( id,updatedTitle,  updatedQuestion,  updatedAnswer,  updatedDate,getActivity());
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new FAQFragment());
                    }
                }
            }
        });

        return view;
    }

}
