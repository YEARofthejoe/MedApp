package com.pc.joe.medapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class TestActivity extends AppCompatActivity implements MyListAdapter.ButtonClickListener{
    ArrayList<Appointment> listOfItem;
    RecyclerView quotesRecyclerView;
    //private ListView listView;
    private AppointmentAdapter appointmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        quotesRecyclerView = findViewById(R.id.recycler_view);
        //Instantiating LinearLayoutManager and MyListAdapter

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfItem = new ArrayList<Appointment>();
                String appointment, time, doctor, status, reason, location;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dsuser.getKey().equals("micks")) {
                                    appointment = "Doctor: "+dstime.child("doctor").getValue()+"\nLocation: "+ dsloc.getKey()+"\nDate/Time: "+dstime.getKey();
                                    time = dstime.getKey();
                                    doctor= dstime.child("doctor").getValue().toString();
                                    location = dsloc.getKey();
                                    status = dstime.child("status").getValue().toString();
                                    reason = dstime.child("reason").getValue().toString();
                                    listOfItem.add(new Appointment(time,dstime.getKey(),
                                            doctor,"Mickel McMillan", location, status,reason,
                                            "Test"));
                                    /*Toast.makeText(getApplicationContext(),listOfItem.size(),
                                            Toast.LENGTH_LONG).show();*/
                                }
                            }

                        }
                    }
                    //appointmentAdapter = new AppointmentAdapter(getBaseContext(),
                    // appointmentList);
                    //appointmentsListView.setAdapter(appointmentAdapter);
                    //Hooking up to RecyclerView
                    MyListAdapter quotesListAdapter = new MyListAdapter(getApplicationContext(),
                            listOfItem,
                            new MyListAdapter.ButtonClickListener() {
                                @Override
                                public void onButtonClick(int position) {

                                }
                            });
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    quotesRecyclerView.setLayoutManager(manager);
                    quotesRecyclerView.setAdapter(quotesListAdapter);
                } catch (Exception e) {
                    Log.e("Data", "Error");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Login", "Issue reaching database");
            }
        });


    }
    @Override
    public void onButtonClick(int position) {
        //You will get position of the item clicked depending on your situation perform a desired action. We are simply Toasting.
        Toast.makeText(this,position + " item clicked! "+listOfItem.indexOf(position),
                Toast.LENGTH_LONG).show();
    }
}
