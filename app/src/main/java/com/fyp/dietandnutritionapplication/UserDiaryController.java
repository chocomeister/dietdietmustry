package com.fyp.dietandnutritionapplication;

import android.util.Log;

import java.sql.Timestamp;

public class UserDiaryController {

    private UserDiary userDiary;

    // Constructor to initialize the UserDiary instance
    public UserDiaryController() {
        this.userDiary = new UserDiary(); // Assuming a no-argument constructor exists in UserDiary
    }

    public void handleDiaryEntry(Timestamp entryDateTime, String mealType, String thoughts, String tags, String username) {
        if (validateDiaryEntry(entryDateTime, mealType, thoughts)) {
            UserDiary userDiary = new UserDiary(entryDateTime, mealType, thoughts, tags, username, null);
            userDiary.saveDiaryEntry();
        } else {
            Log.w("UserDiaryController", "Validation failed for diary entry.");
        }
    }

    public void fetchDiaryEntries(String username, UserDiary.OnDiaryEntriesFetchedListener listener) {
        userDiary.fetchDiaryEntries(username, listener);
    }

    public void deleteDiaryEntry(String diaryID, UserDiaryFragment.OnEntryDeletedListener listener) {
        userDiary.deleteDiaryEntry(diaryID, listener);
    }

    public void updateDiaryEntry(String diaryID, UserDiary entry, UserDiaryFragment.OnEntryUpdatedListener listener) {
        userDiary.updateDiaryEntry(diaryID, entry, listener);
    }


    private boolean validateDiaryEntry(Timestamp entryDateTime, String mealType, String thoughts) {
        return entryDateTime != null && mealType != null && !mealType.isEmpty() && thoughts != null && !thoughts.isEmpty();
    }
}