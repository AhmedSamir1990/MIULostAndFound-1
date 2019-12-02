package com.example.miulostandfound;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class GettingDeviceTokenService extends FirebaseMessagingService {

    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }

}