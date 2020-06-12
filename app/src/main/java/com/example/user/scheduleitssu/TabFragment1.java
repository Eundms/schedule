package com.example.user.scheduleitssu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Subject;
import com.example.user.scheduleitssu.DataClass.What2Do;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TabFragment1 extends Fragment implements Today2doAdapter.OnTodayItemClickListener{
    Context context;
    RecyclerView today2doRecyclerView;
    Today2doAdapter today2doAdapter;
    CalendarView todocalendar;
    TextView selectedday;
    Date now;
    ArrayList<Subject> subjectArrayList= new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    ArrayList<What2Do>today2doArrayList=new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        subjectArrayList=new ArrayList<>();

        View root=inflater.inflate(R.layout.fragment_tab1,container,false);
        this.context=root.getContext();

        today2doRecyclerView=root.findViewById(R.id.today2doRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        today2doRecyclerView.setLayoutManager(layoutManager);
        now =new Date();


        selectedday= root.findViewById(R.id.addwhichdaytodo);
        todocalendar= root.findViewById(R.id.todocalendar);

        todocalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getContext(), ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                selectedday.setText(""+year+"-"+String.format("%02d",month+1)+"-"+String.format("%02d",dayOfMonth));


                //simpledateformat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREA);
                // Z에 대응하여 +0900이 입력되어 문제 생겨 수작업으로 입력
                String datetime = String.valueOf(selectedday.getText());

                new CalendarUtil(datetime).execute();


            }});

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        String uid=user.getDisplayName()+"_"+user.getUid();
        DatabaseReference myRef = database.getReference().child("Student").child(uid).child("SubjectList");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectArrayList.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) { //하위노드가 없을 떄까지 반복
                    Subject subject = dataSnapshot2.getValue(Subject.class);
                    Log.d("TABFRAGMENT2",subject.getClassname());
                    subjectArrayList.add(subject);
                    //subjectAdapter.notifyItemInserted(subjectArrayList.size()-1);
                }
               //Log.d("TABFRAGMENT2",""+subjectArrayList.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  Log.d("TABFRAGMETNT2",""+subjectArrayList.size());


        return root;
    }

    @Override
    public void onTodayItemClick(View v, int pos) {
        Log.d("ONITEM","TA");
    }

    public class todoEvent{

        String title;
        String description;

        public todoEvent(String title,String description){

            this.description=description;
            this.title=title;

        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @NonNull
        @Override
        public String toString() {
            return title+": "+description;
        }
    }

    class CalendarUtil extends AsyncTask<Void, Void, String> {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();


        GoogleAccountCredential credential;
        com.google.api.services.calendar.Calendar service = null;

        ArrayList<What2Do> eventStrings=new ArrayList<>() ;

        String eventDate;


        public CalendarUtil(String eventDate) {

            this.eventDate=eventDate;
            // Google Accounts
            credential =
                    GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccountName(user.getEmail());

            Log.d("calendar1", credential.getSelectedAccountName());

            // Tasks client
            service =
                    new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                            .setApplicationName("Uninote").build();

        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("calendar5",eventStrings.toString());

            today2doAdapter=new Today2doAdapter(context,eventStrings);
            today2doRecyclerView.setAdapter(today2doAdapter);




        }
        @Override
        protected String doInBackground(Void... voids) {

            for(Subject subject:subjectArrayList)
            try {
                    getEvent(subject.getClassname());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("calendar4", eventStrings.toString());

            return null;

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
                    if ( calendarListEntry.getSummary().equals(calendarTitle)) {
                        id = calendarListEntry.getId();
                    }
                }
                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);

            return id;
        }

        private List<todoEvent> getEvent(String calendarTitle) throws IOException {


            String calendarID = getCalendarID(calendarTitle);
            if ( calendarID == null ){

                return null;
            }


            Events events = service.events().list(calendarID)//"primary")
                    .setTimeMin(new DateTime(eventDate+"T00:00:00.000+09:00"))
                    .setTimeMax(new DateTime(eventDate+"T23:59:59.999+09:00"))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            Log.d("calendar2",events.getSummary());


            for (Event event : items) {

                DateTime start = event.getStart().getDateTime();
                if (start == null) {

                    // 모든 이벤트가 시작 시간을 갖고 있지는 않다. 그런 경우 시작 날짜만 사용
                    start = event.getStart().getDate();
                }


                eventStrings.add(new What2Do(event.getSummary(),0));
                //Log.d("calendar3",eventStrings.toString());

            }


            return null;
        }
    }


}
