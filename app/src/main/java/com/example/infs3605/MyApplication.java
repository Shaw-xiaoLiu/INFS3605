package com.example.infs3605;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}
