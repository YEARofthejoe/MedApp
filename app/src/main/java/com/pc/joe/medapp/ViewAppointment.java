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
import android.widget.Toast;

public class ViewAppointment extends AppCompatActivity {
    User user;
    Spinner usersSpinner;
    ArrayList<String> usersList;
    ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User) getIntent().getSerializableExtra("user");
        }
        else{
            Log.e("MakeApp", "A user was not provided to class");
            finish();
        }
        usersSpinner = findViewById(R.id.appointmentusers);
        if(user.getUserType().equals("Patient")){
           usersSpinner.setEnabled(false);
           usersSpinner.setClickable(false);
           Toast.makeText(getApplicationContext(),user.getUserName(),Toast.LENGTH_SHORT).show();

        }
        else{
            /*usersList = new ArrayList<String>();
            usersList.add("Mickel");
            usersAdapter =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,usersList);
            usersSpinner.setAdapter(usersAdapter);*/
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("user/");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersList = new ArrayList<>();
                    String user;

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Log.d("My_Location", "User being checked is " + ds.getKey());
                        //Populate doctor list based on location
                        user = ds.child("firstName").getValue().toString()+" "+ds.child("lastName").getValue().toString();
                        usersList.add(user);
                        /*try {
                            if (dataSnapshot.child(ds.getKey()).child("type").getValue().equals("Doctor")) {

                                user = ds.child("location").getValue().toString();

                                if (!usersList.contains(user) && !user.equals("")) {

                                }

                            }
                        } catch (Exception e){
                            Log.d("My_Location", "User being checked is not a doctor :" + ds.getKey());
                        }*/
                    }

                    usersAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, usersList);
                    usersSpinner.setAdapter(usersAdapter);
                                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Login", "Issue reaching database");
                }
            });


        }

        usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });
    }// end OnCreate
}
