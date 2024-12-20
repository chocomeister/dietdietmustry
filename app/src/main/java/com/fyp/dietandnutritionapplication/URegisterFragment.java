package com.fyp.dietandnutritionapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link URegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class URegisterFragment extends Fragment {

    private EditText firstNameEditText, lastNameEditText, userNameEditText, dobEditText, emailEditText, phoneEditText, passwordEditText, confirmPW;
    private RadioButton maleRadioButton, femaleRadioButton,selectedRadioButtonRole;
    private RadioGroup radioGroupRole;
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

        View view = inflater.inflate(R.layout.fragment_u_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        MainActivity mainActivity = (MainActivity) getActivity();
        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        userNameEditText = view.findViewById(R.id.userNameCreate);
        dobEditText = view.findViewById(R.id.dobtext);
        emailEditText = view.findViewById(R.id.email);
        phoneEditText = view.findViewById(R.id.editTextPhone);
        passwordEditText = view.findViewById(R.id.enterPW);
        confirmPW = view.findViewById(R.id.confirmPW);
        maleRadioButton = view.findViewById(R.id.rbMale);
        femaleRadioButton = view.findViewById(R.id.rbFemale);
        registerButton = view.findViewById(R.id.loginbutton);
        radioGroupRole = view.findViewById(R.id.radioGroupRole);
        RadioButton userRadioButton = view.findViewById(R.id.rbUser);
        RadioButton nutritionistRadioButton = view.findViewById(R.id.rbnutri);

        if (getArguments() != null) {
            String role = getArguments().getString("role");
            if (role != null) {
                if (role.equals("user")) {
                    userRadioButton.setChecked(true); // Automatically select "User" role
                } else if (role.equals("nutritionist")) {
                    nutritionistRadioButton.setChecked(true); // Automatically select "Nutritionist" role
                }
            }
        }

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String userName = userNameEditText.getText().toString();
                String dob = dobEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = passwordEditText.getText().toString();
                int selectedId = radioGroupRole.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    // No RadioButton selected
                    Toast.makeText(getActivity(), "Please select a role.", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedRadioButtonRole = view.findViewById(selectedId);
                String selectedRole = selectedRadioButtonRole.getText().toString();
                @SuppressLint({"NewApi", "LocalSuppress"}) String date = LocalDateTime.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(dob) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gender;
                if (maleRadioButton.isChecked()) {
                    gender = "Male";
                } else if (femaleRadioButton.isChecked()) {
                    gender = "Female";
                } else {
                    gender = "Unspecified";
                }

                URegisterController uRegisterController = new URegisterController();
                uRegisterController.checkRegister(firstName, lastName,  userName, dob, email, phone, gender, password, date, getActivity());

                }

        });

        dobEditText.setOnClickListener(v -> showDatePickerDialog(dobEditText));

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

    private void showDatePickerDialog(EditText dobEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(Calendar.YEAR, selectedYear);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calendar.getTime());

            dobEditText.setText(formattedDate);
        }, year, month, day);

        // Set the date range for the DatePickerDialog
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, 1900); // Set the minimum year (e.g., 1900)

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.YEAR, 2024); // Set the maximum year to 2024

        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        // Show the dialog
        datePickerDialog.show();
    }


}