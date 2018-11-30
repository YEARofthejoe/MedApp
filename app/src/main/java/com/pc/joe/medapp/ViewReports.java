package com.pc.joe.medapp;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.text.DateFormatSymbols;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Set;

public class ViewReports extends Activity {

    String yearString[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        int years = Calendar.getInstance().get(Calendar.YEAR);
        yearString = new String[5];

        //Get the years
        for (int i = 0; i <= 4; i++) {
            yearString[i] = String.valueOf(years + (i - 2));
        }

        final Spinner yearSpinner = findViewById(R.id.spinnerReportYear);
        final Spinner locationSpinner = findViewById(R.id.spinnerReportLocation);
        Button runButton = findViewById(R.id.buttonReportRun);

        SpinnerAdapter spinAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, yearString);
        yearSpinner.setAdapter(spinAdapter);

        final TextView textViewBIGTEXT = findViewById(R.id.textViewBIGTEXT);
        textViewBIGTEXT.setMovementMethod(new ScrollingMovementMethod());

        //get locations and doctors
        //connect to the database for the list we need
        //User locations and doctors
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user/");

        //set the spinners
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> locations = new ArrayList<>();
                Hashtable<String, ArrayList<String>> doctorMap = new Hashtable<>();
                ArrayList<String> doctorsAtLocations = new ArrayList<>();
                String location;

                //loop through and get locations
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("My_Location", "User being checked is " + ds.getKey());
                    //Populate doctor list based on location
                    try {
                        String userString;
                        userString = dataSnapshot.child(ds.getKey()).child("type").getValue().toString();

                        //get a list of locations that have doctors
                        if (userString.equals("Doctor")) {
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
                    } catch (Exception e) { //probably the easiest way to do this
                        Log.d("My_Location", "User being checked is not a doctor :" + ds.getKey());
                    }

                    SpinnerAdapter spinAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, locations);
                    locationSpinner.setAdapter(spinAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Login", "Issue reaching database");
            }
        });

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String year = yearSpinner.getSelectedItem().toString();
                final String loc = locationSpinner.getSelectedItem().toString();

                DatabaseReference locRef = database.getReference("appointment/");

                locRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Read appointments and do some STATS
                        int totalToDate = 0;
                        Hashtable<String, Hashtable<String, Integer>> statsHash = new Hashtable<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (DataSnapshot ds2 : ds.getChildren()) {
                                for (DataSnapshot ds3 : ds2.getChildren()) {
                                    try{ //we cant assume the data is ok, even tho i make it...
                                        Log.i("database_print", "Appointment Name:" + ds3.getKey() + " Doctor:" + ds3.child("doctor").getValue().toString() + " Appointee:" + ds2.getKey());
                                        //Check we are at the right location
                                        if(ds.getKey().equals(loc)){
                                            //Lets see if our year matches.. I am using contains because I did not use trailing zeros in months.. I could use regex but this should be safe enough
                                            if (ds3.getKey().contains(year)) {
                                                //Check out doctors name and either add him or up his count
                                                totalToDate++;

                                                //Take the appointment and sort it by month
                                                String[] splitString = ds3.getKey().split("-");

                                                String month = splitString[0];

                                                //Check if we already have this month
                                                if(!statsHash.containsKey(month)){
                                                    statsHash.put(month, new Hashtable<String, Integer>());
                                                    statsHash.get(month).put(ds3.child("doctor").getValue().toString(), 1);
                                                }
                                                else{
                                                    statsHash.get(month).put(ds3.child("doctor").getValue().toString(), statsHash.get(month).get(ds3.child("doctor").getValue().toString()) + 1);
                                                }
                                            }
                                        }
                                    } catch(Exception e) {
                                        //There was probably a malformed record
                                        Log.w("data_error", "Something is wrong with the record " + ds3.getKey() + " for " +ds2.getKey() + "@" + ds.getKey());
                                    }
                                }
                            }
                        }
                        //Lets build some crazy string and populate it the listView
                        //Total appointments
                        String BIGSTRING;

                        BIGSTRING = "Data@location:"+loc+" at the year:"+year+"\n";
                        BIGSTRING = BIGSTRING +"------------------------------"+"\n"+"\n"+"\n";
                        BIGSTRING = BIGSTRING + "The total Number of appointments this year is " + totalToDate+"\n";
                        //Print out the doctor stats
                        Set<String> keys = statsHash.keySet();
                        for(String key: keys){
                            String stringMonth = new DateFormatSymbols().getMonths()[Integer.parseInt(key)-1];
                            BIGSTRING = BIGSTRING +"------------------------------"+"\n"+"\n"+"\n";
                            BIGSTRING = BIGSTRING + "Month:"+stringMonth+"\n";
                            Set<String> keys2 = statsHash.get(key).keySet();
                            for(String key2 : keys2){
                                BIGSTRING = BIGSTRING +"-"+"\n";
                                BIGSTRING = BIGSTRING + "Doctor:" + key2 + " Appointments: "+statsHash.get(key).get(key2)+"\n";
                            }
                        }


                        textViewBIGTEXT.setText(BIGSTRING);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
