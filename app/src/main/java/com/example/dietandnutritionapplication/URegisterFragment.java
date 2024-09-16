package com.example.dietandnutritionapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link URegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class URegisterFragment extends Fragment {

    private EditText firstNameEditText, userNameEditText, dobEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button registerButton;
    private ArrayList<Profile> accountArray = new ArrayList<>();
    FirebaseAuth mAuth;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public URegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static URegisterFragment newInstance(String param1, String param2) {
        URegisterFragment fragment = new URegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_u_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        MainActivity mainActivity = (MainActivity) getActivity();
        firstNameEditText = view.findViewById(R.id.firstName);
        userNameEditText = view.findViewById(R.id.userNameCreate);
        dobEditText = view.findViewById(R.id.dobtext);
        emailEditText = view.findViewById(R.id.email);
        phoneEditText = view.findViewById(R.id.editTextPhone);
        passwordEditText = view.findViewById(R.id.enterPW);
        confirmPasswordEditText = view.findViewById(R.id.cfmPW);
        maleRadioButton = view.findViewById(R.id.rbMale);
        femaleRadioButton = view.findViewById(R.id.rbFemale);
        registerButton = view.findViewById(R.id.loginbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the EditTexts
                String firstName = firstNameEditText.getText().toString();
                String userName = userNameEditText.getText().toString();
                String dob = dobEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(getActivity(), "Enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getActivity(), "Enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(getActivity(), "Enter your birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(password, confirmPassword)) {
                    Toast.makeText(getActivity(), "Please ensure passwords match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check which RadioButton is selected (gender)
                String gender;
                if (maleRadioButton.isChecked()) {
                    gender = "Male";
                } else if (femaleRadioButton.isChecked()) {
                    gender = "Female";
                } else {
                    gender = "Unspecified"; // Handle case where no gender is selected
                }
            }
        });


        TextView myTextView = view.findViewById(R.id.haveacct);
        myTextView.setClickable(true);
        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new LoginFragment());
            }
        });

        return view;
    }
}