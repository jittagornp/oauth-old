/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.firebase.authen;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/27
 */
public class AppAuthen {

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        AppAuthen app = new AppAuthen();
        app.authen();

        String token = app.getTokenForUserId("1");
        System.out.println("token => " + token);
    }

    public void authen() throws IOException {
        try (InputStream serviceAccount = getClass().getResourceAsStream("/pamarin-firebase-firebase-adminsdk-x7qja-c7069f15a6.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://pamarin-firebase.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
    
    public String getTokenForUserId(String userId) throws InterruptedException, ExecutionException{
        return FirebaseAuth.getInstance().createCustomTokenAsync(userId).get();
    }

}
