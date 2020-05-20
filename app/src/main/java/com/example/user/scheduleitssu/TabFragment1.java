package com.example.user.scheduleitssu;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.What2Do;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class TabFragment1 extends Fragment{
    Context context;
    RecyclerView today2doRecyclerView;
    Today2doAdapter today2doAdapter;
    CalendarView todocalendar;
    EditText selectedday;
    Date now;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View root=inflater.inflate(R.layout.fragment_tab1,container,false);
        this.context=root.getContext();

        today2doRecyclerView=root.findViewById(R.id.today2doRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        today2doRecyclerView.setLayoutManager(layoutManager);
        now =new Date();
        ArrayList<What2Do>today2doArrayList=new ArrayList<>();
        today2doArrayList.add(new What2Do("1)(default)", now.getTime()));
        today2doArrayList.add(new What2Do("2)(default)", now.getTime()));
        today2doArrayList.add(new What2Do("3)(default)", now.getTime()));
        today2doArrayList.add(new What2Do("4)(default)", now.getTime()));
        today2doArrayList.add(new What2Do("5)(default)", now.getTime()));

        today2doAdapter=new Today2doAdapter(this.getContext(),today2doArrayList);
        today2doRecyclerView.setAdapter(today2doAdapter);
        selectedday=(EditText)root.findViewById(R.id.addwhichdaytodo);
        todocalendar=(CalendarView)root.findViewById(R.id.todocalendar);

        todocalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getContext(), ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                selectedday.setText(""+year+"/"+(month+1)+"/"+dayOfMonth);
            }});


        return root;
    }


    class CalendarUtil extends AsyncTask<Void, Void, String> {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        GoogleAccountCredential credential;
        com.google.api.services.calendar.Calendar service = null;

        String title;
        String description;


        public CalendarUtil(String title, String description) {


            this.title = title;
            this.description = description;

            // Google Accounts
            credential =
                    GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccountName(user.getEmail());

            Log.d("calendar", credential.getSelectedAccountName().toString());

            // Tasks client
            service =
                    new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                            .setApplicationName("Uninote").build();

        }

        @Override
        protected String doInBackground(Void... voids) {

            ArrayList<String> calendarList=new ArrayList<>();

            return "";
        }

        private String getCalendarID(String calendarTitle){
            String id = null;
            // Iterate through entries in calendar list
            String pageToken = null;
            do {
                CalendarList calendarList = null;
                try {
                    calendarList = service.calendarList().list().setPageToken(pageToken).execute();
                } catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), 1001);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                List<CalendarListEntry> items = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : items) {
                    if ( calendarListEntry.getSummary().toString().equals(calendarTitle)) {
                        id = calendarListEntry.getId().toString();
                    }
                }
                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);

            return id;
        }
    }
}
