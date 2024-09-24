package com.example.dietandnutritionapplication;

import android.widget.Toast;

import java.util.ArrayList;

public class ViewAccountsController {
    ArrayList<Profile> profiles = new ArrayList<>();
    public ViewAccountsController(){

    }
    public void retrieveAccounts(final UserAccountEntity.DataCallback callback) {
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.fetchAccounts(new UserAccountEntity.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Profile> accounts) {
                profiles.clear();
                profiles.addAll(accounts);
                callback.onSuccess(profiles);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                callback.onFailure(e);
            }
        });
    }
}
