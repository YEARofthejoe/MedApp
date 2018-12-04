package com.pc.joe.medapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
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
    ArrayList<Appointment> usersList, appointmentList, appointmentList2;
    AppointmentAdapter appointmentAdapter;
    DoctorAppointmentAdapter doctorAppointmentAdapter;
    ReceptionistAppointmentAdapter receptionistAppointmentAdapter;
    ListView appointmentsListView;
    TextView userLoggedIn, loc;
    String username, userType;
    Button testButton;
    String receptionistLocation;
    RecyclerView appointmentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);
        userLoggedIn = findViewById(R.id.currentuser);
        //appointmentsListView = findViewById(R.id.appointments);
        loc = findViewById(R.id.loc);
        //testButton = findViewById(R.id.test_button);
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User) getIntent().getSerializableExtra("user");
            if(user.getUserType().equals("Patient"))
                userLoggedIn.setText("Displaying Appointments for Patient: "+user.getFirstName()+" "+user.getLastName());
            else if (user.getUserType().equals("Doctor"))
                userLoggedIn.setText("Displaying Appointments for Doctor: "+user.getFirstName()+" "+user.getLastName());
            else
                getReceptionistLocation();
                //userLoggedIn.setText("Displaying Appointments for Receptionist: "+user.getFirstName()+" "+user.getLastName()+" at location");
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
        else if(userType.equals("Receptionist")){
            getReceptionistAppointments();
            //Toast.makeText(getApplicationContext(),getReceptionistLocation(),Toast.LENGTH_SHORT).show();
        }

        //appointmentsRecyclerView.getRecycledViewPool().clear();
        /*usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        }); */
        /*testButton.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view) {
                Intent appointmentSetupIntent = new Intent(ViewAppointment.this, TestActivity.class);
                appointmentSetupIntent.putExtra("user", user);
                ViewAppointment.this.startActivity(appointmentSetupIntent);
            }
        }); */
    }// end OnCreate

    public void getPatientAppointments(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment/");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<Appointment>();
                String appointment, time, doctor, status, reason, location;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dsuser.getKey().equals(username)) {
                                    appointment = "Doctor: "+dstime.child("doctor").getValue()+"\nLocation: "+ dsloc.getKey()+"\nDate/Time: "+dstime.getKey();
                                    time = dstime.getKey();
                                    doctor= dstime.child("doctor").getValue().toString();
                                    location = dsloc.getKey();
                                    status = dstime.child("status").getValue().toString();
                                    reason = dstime.child("reason").getValue().toString();
                                    appointmentList.add(new Appointment(time,username,doctor,
                                            user.getFirstName()+" "+user.getLastName(), location, status,reason));
                                    //Toast.makeText(getApplicationContext(), time,
                                            //Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    PatientViewAppointmentsAdapter appointmentListAdapter =
                            new PatientViewAppointmentsAdapter(getApplicationContext(),
                            appointmentList,
                            new PatientViewAppointmentsAdapter.ButtonClickListener() {
                                @Override
                                public void onButtonClick(int position) {

                                }
                            });
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    appointmentsRecyclerView.setLayoutManager(manager);
                    appointmentsRecyclerView.setAdapter(appointmentListAdapter);

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
        final DatabaseReference appointmentTableRef = database.getReference("appointment/");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<Appointment>();
                String appointment, date, patient, status, reason, size;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dstime.child("doctor").getValue().equals(username)) {
                                    /*appointment =
                                            "Patient: "+ dsuser.getKey()+"\nDate/Time: "+dstime
                                            .getKey()+"\nReason: "+dstime.child("reason")
                                            .getValue()+"\n"; */
                                    date = dstime.getKey();
                                    patient = dsuser.getKey();
                                    status = dstime.child("status").getValue().toString();
                                    reason = dstime.child("reason").getValue().toString();
                                    appointmentList.add(new Appointment(date,username,username,
                                            patient,
                                            dsloc.getKey()
                                            , status,reason));
                                    /*Toast.makeText(getApplicationContext(),
                                            date+patient+status+reason,
                                            Toast.LENGTH_SHORT).show(); */
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    Log.e("Data", "Error");
                }
                Toast.makeText(getApplicationContext(), "Here",
                        Toast.LENGTH_SHORT).show();
                PatientViewAppointmentsAdapter doctorAppointmentListAdapter =
                        new PatientViewAppointmentsAdapter(getApplicationContext(),
                                appointmentList,
                                new PatientViewAppointmentsAdapter.ButtonClickListener() {
                                    @Override
                                    public void onButtonClick(int position) {

                                    }
                                });
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false);
                appointmentsRecyclerView.setLayoutManager(manager);
                appointmentsRecyclerView.setAdapter(doctorAppointmentListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Login", "Issue reaching database");
            }
        });
    }

    public void getReceptionistLocation(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("user/"+user.getUserName());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.exists()){

                        loc.setText(dataSnapshot.child("location").getValue().toString());
                        userLoggedIn.setText("Displaying Appointments at "+ dataSnapshot.child("location").getValue().toString() +" for Receptionist: "+user.getFirstName()+" "+user.getLastName()+" ");
                        //Toast.makeText(getApplicationContext(), receptionistLocation,Toast.LENGTH_SHORT).show();

                    }

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

    public void getReceptionistAppointments(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment");

        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<Appointment>();
                String appointment, time, doctor, status, reason, patient;
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        if (dsloc.getKey().equals(loc.getText().toString())) {
                            for (DataSnapshot dsuser : dsloc.getChildren()) {
                                for (DataSnapshot dstime : dsuser.getChildren()) {
                                    appointment = "Doctor: " + dstime.child("doctor").getValue() + "\nLocation: " + dsloc.getKey() + "\nDate/Time: " + dstime.getKey();
                                    time = dstime.getKey();
                                    doctor = dstime.child("doctor").getValue().toString();
                                    patient = dsuser.getKey();
                                    status = dstime.child("status").getValue().toString();
                                    //reason = dstime.child("reason").getValue().toString();
                                    appointmentList.add(new Appointment(time, patient, patient,
                                            patient, doctor, status, ""));

                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("Data", "Error");
                }
                PatientViewAppointmentsAdapter receptionistAppointmentListAdapter =
                        new PatientViewAppointmentsAdapter(getApplicationContext(),
                                appointmentList,
                                new PatientViewAppointmentsAdapter.ButtonClickListener() {
                                    @Override
                                    public void onButtonClick(int position) {

                                    }
                                });
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false);
                appointmentsRecyclerView.setLayoutManager(manager);
                appointmentsRecyclerView.setAdapter(receptionistAppointmentListAdapter);

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
