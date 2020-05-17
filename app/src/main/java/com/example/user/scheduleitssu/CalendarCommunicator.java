package com.example.user.scheduleitssu;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.google.api.services.calendar.Calendar.CalendarList.*;

public class CalendarCommunicator  {


    Context context;
    HttpTransport transport = AndroidHttp.newCompatibleTransport();
    JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    GoogleAccountCredential credential;
    Calendar service = null;




    public CalendarCommunicator(Context context){

        this.context=context;
        // Google Accounts
        credential =
                GoogleAccountCredential.usingOAuth2(this.context, Collections.singleton(CalendarScopes.CALENDAR));
        credential.setSelectedAccountName(user.getEmail());

        // Tasks client
        service =
                new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                        .setApplicationName("Uninote").build();

    }

}
