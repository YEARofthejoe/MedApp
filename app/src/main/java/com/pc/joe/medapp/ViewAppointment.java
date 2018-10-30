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
import android.widget.ListView;

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
    ArrayList<String> usersList, appointmentList;
    ArrayAdapter<String> usersAdapter, appointmentAdapter;
    ListView appointmentsListView;
    TextView userLoggedIn;
    String username, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);
        userLoggedIn = findViewById(R.id.currentuser);
        appointmentsListView = findViewById(R.id.appointments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User) getIntent().getSerializableExtra("user");
            userLoggedIn.setText(user.getUserType()+" "+user.getUserName());
        } else {
            Log.e("MakeApp", "A user was not provided to class");
            finish();
        }

        /*if(user.getUserType().equals("Patient")){
           usersSpinner.setEnabled(false);
           usersSpinner.setClickable(false);
           Toast.makeText(getApplicationContext(),user.getUserName(),Toast.LENGTH_SHORT).show();

        }*/
        username = user.getUserName();
        userType = user.getUserType();
        if(userType.equals("Patient")){
            //Toast.makeText(getApplicationContext(),user.getUserName(),Toast.LENGTH_SHORT).show();
            getPatientAppointments();
        }
        else if(userType.equals("Doctor")){
            Toast.makeText(getApplicationContext(),user.getUserName(),Toast.LENGTH_SHORT).show();
            getDoctorAppointments();
        }


        /*usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });*/
    }// end OnCreate

    public void getPatientAppointments(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<>();
                String appointment;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dsuser.getKey().equals(username)) {
                                    appointment = dstime.getKey();
                                    appointmentList.add(appointment);
                                }
                            }

                        }
                    }
                    appointmentAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, appointmentList);
                    appointmentsListView.setAdapter(appointmentAdapter);
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

    public void getDoctorAppointments(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<>();
                String appointment;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dstime.child("doctor").getValue().equals(username)) {
                                    appointment = "Patient: "+ dsuser.getKey()+"\nDate/Time: "+dstime.getKey()+"\nReason: "+dstime.child("reason").getValue()+"\n";
                                    appointmentList.add(appointment);
                                }
                            }

                        }
                    }
                    appointmentAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, appointmentList);
                    appointmentsListView.setAdapter(appointmentAdapter);
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
}


 /*   else{
            usersList = new ArrayList<String>();
            usersList.add("Mickel");
            usersAdapter =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,usersList);
            usersSpinner.setAdapter(usersAdapter);
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("user/");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        usersList = new ArrayList<>();
        appointmentList = new ArrayList<>();
        String user;

        for(DataSnapshot ds : dataSnapshot.getChildren()) {
        //Log.d("My_Location", "User being checked is " + ds.getKey());
        //Populate doctor list based on location
        user = ds.child("firstName").getValue().toString()+" "+ds.child("lastName").getValue().toString();
        usersList.add(user);
        }

        usersAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, usersList);
        usersSpinner.setAdapter(usersAdapter);
        }

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e("Login", "Issue reaching database");
        }
        });

*/
