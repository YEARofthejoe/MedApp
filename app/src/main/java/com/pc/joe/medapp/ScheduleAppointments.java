package com.pc.joe.medapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class ScheduleAppointments extends AppCompatActivity {

    User user;
    Spinner locSpinner, docSpinner;
    ArrayList<String> locations, doctorsAtLocations;
    ArrayAdapter<String> locAdapter, spinAdapter;
    Hashtable <String, ArrayList<String>> doctorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User) getIntent().getSerializableExtra("user");
        }
        else{
            Log.e("MakeApp", "A user was not provided to class");
            finish();
        }

        locSpinner = findViewById(R.id.makeLocSpinner);
        docSpinner = findViewById(R.id.makeDocSpinner);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user/");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations = new ArrayList<>();
                doctorMap = new Hashtable<>();
                doctorsAtLocations = new ArrayList<>();
                String location;

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("My_Location", "User being checked is " + ds.getKey());
                    //Populate doctor list based on location
                    try {
                        if (dataSnapshot.child(ds.getKey()).child("type").getValue().equals("Doctor")) {

                            location = ds.child("location").getValue().toString();

                            if (!locations.contains(location) && !location.equals("")) {
                                locations.add(location);
                            }

                            doctorsAtLocations.clear();

                            //get all the doctor users.
                            if (doctorMap.contains(location)) {
                                Log.d("My_Location", ds.getKey() + " being added to existing list " + location);
                                doctorMap.get(location).add(ds.getKey());
                            } else {
                                Log.d("My_Location", ds.getKey() + " being added to new list " + location);
                                doctorsAtLocations.add(ds.getKey());
                                doctorMap.put(location, new ArrayList<String>());
                                doctorMap.get(location).add(ds.getKey());
                            }
                        }
                    } catch (Exception e){
                        Log.d("My_Location", "User being checked is not a doctor :" + ds.getKey());
                    }
                }

                locAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, locations);
                locSpinner.setAdapter(locAdapter);

                doctorsAtLocations = doctorMap.get(locSpinner.getSelectedItem());
                spinAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, doctorsAtLocations);
                docSpinner.setAdapter(spinAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Login", "Issue reaching database");
            }
        });

        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorsAtLocations = doctorMap.get(locSpinner.getSelectedItem());
                spinAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, doctorsAtLocations);
                docSpinner.setAdapter(spinAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });
    }

}
