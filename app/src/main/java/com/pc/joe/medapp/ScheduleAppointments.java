package com.pc.joe.medapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class ScheduleAppointments extends AppCompatActivity {

    User user;
    Spinner locSpinner, docSpinner;
    ArrayList<String> locations, doctorsAtLocations, userList;
    ArrayAdapter<String> locAdapter, spinAdapter;
    Hashtable<String, ArrayList<String>> doctorMap;
    EditText dateEditText, nameEditText, reasonEditText;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener date;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User) getIntent().getSerializableExtra("user");
        } else {
            Log.e("MakeApp", "A user was not provided to class");
            finish();
        }

        //vars
        locSpinner = findViewById(R.id.makeLocSpinner);
        docSpinner = findViewById(R.id.makeDocSpinner);
        dateEditText = findViewById(R.id.makeDateEditText);
        nameEditText = findViewById(R.id.makeNameEditText);
        submitButton = findViewById(R.id.makeSubmitButton);
        reasonEditText = findViewById(R.id.makeReasonEditText);

        //don't let Patient schedule for other people
        if (user.getUserType().equals("Patient")) {
            nameEditText.setAlpha(.5f);
            nameEditText.setFocusable(false);
            nameEditText.setText(user.getUserName());
        }

        //we don't want them to click on this
        dateEditText.setFocusable(false);

        //get our date ready and picker created
        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            //when they are done picking the date
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                //get the date the picked
                calendar.set(year, monthOfYear, dayOfMonth);

                //get ready to let them pick the time
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ScheduleAppointments.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //when they pick the time
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, getNear15Minute(selectedMinute));
                        dateEditText.setText(calendar.toString());
                        //we need to let them know we rounded and set it to the screen
                        dateEditText.setText(getStringDate(calendar));
                        Toast.makeText(getApplicationContext(), "Times rounded down to nearest 15 minutes", Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        //data field
        dateEditText.setOnClickListener(new View.OnClickListener() {

            //when you click on it
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //show the data picker
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(ScheduleAppointments.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        //connect to the database for the list we need
        //User locations and doctors
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user/");
        final DatabaseReference locRef = database.getReference("appointment/");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations = new ArrayList<>();
                doctorMap = new Hashtable<>();
                doctorsAtLocations = new ArrayList<>();
                userList = new ArrayList<>();
                String location;

                //loop through
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("My_Location", "User being checked is " + ds.getKey());
                    //Populate doctor list based on location
                    try {
                        String userString;
                        userString = dataSnapshot.child(ds.getKey()).child("type").getValue().toString();

                        //get a list of patients
                        if (userString.equals("Patient")) {
                            userList.add(ds.getKey());
                        }

                        //get a list of locations that have doctors
                        if (userString.equals("Doctor")) {
                            location = ds.child("location").getValue().toString();

                            if (!locations.contains(location) && !location.equals("")) {
                                locations.add(location);
                            }

                            doctorsAtLocations.clear();

                            //get all the doctor users.
                            if (doctorMap.containsKey(location)) {
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
                }

                //set up the spinners for the first run
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

        //when they change the location
        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorsAtLocations = doctorMap.get(locSpinner.getSelectedItem());
                spinAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, doctorsAtLocations);
                docSpinner.setAdapter(spinAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //submit button logic
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);

                //get todays date
                Boolean okToInsert = true;
                Calendar today = Calendar.getInstance();

                dateEditText.setError(null);
                reasonEditText.setError(null);

                //check the fields
                String dateString = dateEditText.getText().toString();
                if (dateString.equals("")) {
                    dateEditText.setError("Must be entered");
                    okToInsert = false;
                } else if (calendar.before(today)) {
                    dateEditText.setError("You cannot go backwards in time");
                    Toast.makeText(getApplicationContext(), "Time cannot be in the past", Toast.LENGTH_SHORT).show();
                    okToInsert = false;
                }

                if (reasonEditText.getText().toString().equals("")) {
                    reasonEditText.setError("Must enter a reason");
                    okToInsert = false;
                }

                String username = nameEditText.getText().toString();
                if (username.equals("")) {
                    okToInsert = false;
                    nameEditText.setError("Must enter a username");
                } else if (!userList.contains(username)) {
                    okToInsert = false;
                    nameEditText.setError("Not a valid username");
                }

                //we are good to go, lets make sure doctor or patient isn't busy
                if (okToInsert) {
                    Log.i("App_setup", "All input tests passed for this appointment");


                    //we already had this setup before lets reuse it since Final
                    locRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> docAppointmentArrayList = new ArrayList<>();
                            ArrayList<String> patAppointmentArrayList = new ArrayList<>();

                            String doc = docSpinner.getSelectedItem().toString();
                            String person = nameEditText.getText().toString();
                            String loc = locSpinner.getSelectedItem().toString();
                            String appointmentName = dateEditText.getText().toString();

                            Boolean clear = true;
                            Boolean okToInsert = true;

                            //Read appointments
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot ds2 : ds.getChildren()) {
                                    for (DataSnapshot ds3 : ds2.getChildren()) {
                                        try { //we cant assume the data is ok, even tho i make it...
                                            Log.i("database_print", "Appointment Name:" + ds3.getKey() + " Doctor:" + ds3.child("doctor").getValue().toString() + " Appointee:" + ds2.getKey());
                                            if (ds3.getKey().equals(appointmentName) && ds2.getKey().equals(person)) {
                                                Toast.makeText(getApplicationContext(), "You already have this time booked", Toast.LENGTH_SHORT).show();
                                                okToInsert = false;
                                            }
                                            if (ds3.getKey().equals(appointmentName) && ds3.child("doctor").getValue().toString().equals(doc)) {
                                                Toast.makeText(getApplicationContext(), "The doctor already has this time booked", Toast.LENGTH_SHORT).show();
                                                okToInsert = false;
                                            }
                                        } catch (Exception e) {
                                            //There was probably a malformed record
                                            Log.w("data_error", "Something is wrong with the record " + ds3.getKey() + " for " + ds2.getKey() + "@" + ds.getKey());
                                        }
                                    }
                                }
                            } //end appointment check

                            if (okToInsert) {
                                Log.i("App_setup", "All database tests passed for this appointment");
                                locRef.child(loc).child(person).child(appointmentName).child("doctor").setValue(doc);
                                locRef.child(loc).child(person).child(appointmentName).child("note").setValue("none");
                                locRef.child(loc).child(person).child(appointmentName).child("prescription").setValue(false);
                                locRef.child(loc).child(person).child(appointmentName).child("reason").setValue(reasonEditText.getText().toString());
                                locRef.child(loc).child(person).child(appointmentName).child("status").setValue("N");
                                Log.d("Insert", "Record inserted");
                                finish();
                                Toast.makeText(getApplicationContext(), "Booked!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                submitButton.setEnabled(true);
            }
        });

    }

    //For showing the user a time format they probably like
    public static String convertTimetoAMPM(int hour, int min) {
        DateFormat dateFormat = new SimpleDateFormat("h:mm a");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();

        return dateFormat.format(d);
    }

    //do some rounding
    public static int getNear15Minute(int minutes) {
        int mod = minutes % 15;
        int res = 0;
        if ((mod) >= 8) {
            res = minutes + (15 - mod);
        } else {
            res = minutes - mod;
        }
        return (res % 60);
    }

    //return a useful format
    public static String getStringDate(Calendar c) {
        String returnValue;
        returnValue = (c.get(Calendar.MONTH) + 1) + "";
        returnValue += "-" + c.get(Calendar.DAY_OF_MONTH);
        returnValue += "-" + c.get(Calendar.YEAR);
        returnValue += " : " + convertTimetoAMPM(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

        return returnValue;
    }

}
