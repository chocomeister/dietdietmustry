package com.example.dietandnutritionapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Context;

import java.util.ArrayList;


public class UserAccountEntity {
    private FirebaseFirestore db;
    private ArrayList<Admin> admins = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Nutritionist> nutritionists = new ArrayList<>();
    private ArrayList<Profile> accounts = new ArrayList<>();
    FirebaseAuth mAuth;


    public UserAccountEntity() {

        db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Profile> accounts);
        void onFailure(Exception e);
    }

    public void retrieveAndClassifyUsers(final DataCallback callback) {
        db.collection("Users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            admins.clear();
                            users.clear();
                            nutritionists.clear();
                            accounts.clear();

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String role = document.getString("role");

                                switch (role) {
                                    case "user":
                                        User user = createUserFromDocument(document);
                                        users.add(user);
                                        accounts.add(user);
                                        break;
                                    case "admin":
                                        Admin admin = createAdminFromDocument(document);
                                        admins.add(admin);
                                        accounts.add(admin);
                                        break;
                                    case "nutritionist":
                                        Nutritionist nutritionist = createNutritionistFromDocument(document);
                                        nutritionists.add(nutritionist);
                                        accounts.add(nutritionist);
                                        break;
                                }
                            }

                            callback.onSuccess(accounts);
                        } else {
                            callback.onFailure(new Exception("QuerySnapshot is null"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    private User createUserFromDocument(QueryDocumentSnapshot document) {
        User user = new User();
        user.setUsername(document.getString("username"));
        user.setRole(document.getString("role"));
        user.setFirstName(document.getString("firstName"));
        user.setLastName(document.getString("lastName"));
        user.setPhoneNumber(document.getString("phoneNumber"));
        user.setDob(document.getString("dob"));
        user.setPassword(document.getString("password"));
        user.setEmail(document.getString("email"));
        user.setGender(document.getString("gender"));
        user.setRole(document.getString("role"));
        user.setDateJoined(document.getString("dateJoined"));
        user.setCalorieLimit(document.getLong("calorieLimit").intValue());
        user.setDietaryPreference(document.getString("dietPreference"));
        user.setFoodAllergies(document.getString("healthGoal"));
        user.setHealthGoal(document.getString("foodAllergies"));
        user.setCurrentWeight(document.getDouble("currentWeight"));
        user.setCurrentHeight(document.getDouble("currentHeight"));
        user.setActivityLevel(document.getString("activityLevel"));
        return user;
    }

    private Admin createAdminFromDocument(QueryDocumentSnapshot document) {
        return new Admin(
                document.getString("firstName"),
                document.getString("lastName"),
                document.getString("username"),
                document.getString("phoneNumber"),
                document.getString("email"),
                document.getString("gender"),
                document.getString("role"),
                document.getString("dateJoined")
        );
    }

    private Nutritionist createNutritionistFromDocument(QueryDocumentSnapshot document) {
        // Assuming profilePicture is a string path; handle conversion if needed
//        return new Nutritionist(
//                document.getString("firstName"),
//                document.getString("lastName"),
//                document.getString("username"),
//                document.getString("phoneNumber"),
//                document.getString("password"),
//                document.getString("email"),
//                document.getString("gender"),
//                document.getString("role"),
//                document.getString("dateJoined"),
//                document.getString("education"),
//                document.getString("contactInfo"),
//                document.getString("expertise"),
//                document.getString("bio"),
//                document.getString("profilePicture") // Convert to Bitmap if needed
//        );
        Nutritionist user = new Nutritionist();
        user.setUsername(document.getString("username"));
        user.setRole(document.getString("role"));
        user.setLastName(document.getString("lastName"));
        user.setPhoneNumber(document.getString("phoneNumber"));
        user.setPassword(document.getString("password"));
        user.setEmail(document.getString("email"));
        user.setGender(document.getString("gender"));
        user.setRole(document.getString("role"));
        user.setDateJoined(document.getString("dateJoined"));
        user.setEducation(document.getString("education"));
        user.setContactInfo(document.getString("contactInfo"));
        user.setExpertise(document.getString("expertise"));
        user.setBio(document.getString("bio"));
        user.setProfilePicture(document.getString("profilePicture"));

        return user;
    }


    public void fetchAccounts(DataCallback callback) {
        retrieveAndClassifyUsers(callback);
    }


    public void registerUser(String firstName, String lastName, String userName, String dob, String email, String phone, String gender, String password, String datejoined, Context context, RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            User userCreate = new User();
                            userCreate.setFirstName(firstName);
                            userCreate.setLastName(lastName);
                            userCreate.setUsername(userName);
                            userCreate.setDob(dob);
                            userCreate.setPassword(password);
                            userCreate.setEmail(email);
                            userCreate.setPhoneNumber(phone);
                            userCreate.setGender(gender);
                            userCreate.setDateJoined(datejoined);


                            db.collection("Users").document(userId).set(userCreate)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure("Failed to save user data");
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public interface RegisterCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public void addAdmin(String firstName, String lastName, String userName, String dob, String email, String phone, String gender, String password, String date, Context context, RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            Admin adminCreate = new Admin();
                            adminCreate.setFirstName(firstName);
                            adminCreate.setLastName(lastName);
                            adminCreate.setUsername(userName);
                            adminCreate.setDob(dob);
                            adminCreate.setPassword(password);
                            adminCreate.setEmail(email);
                            adminCreate.setPhoneNumber(phone);
                            adminCreate.setGender(gender);
                            adminCreate.setDateJoined(date);
                            adminCreate.setRole("admin");


                            db.collection("Users").document(userId).set(adminCreate)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure("Failed to save user data");
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

}