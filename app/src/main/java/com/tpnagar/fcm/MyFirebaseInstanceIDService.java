package com.tpnagar.fcm;



import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseInsIDService";
    String refreshedToken;

    @Override
    public void onNewToken(@NonNull String token) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        //   sendFCMTokenToDatabase(task.getResult());
                        refreshedToken = task.getResult();
                        // Log.d(TAG, "New Token: " + refreshedToken);
                    }
                });


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
