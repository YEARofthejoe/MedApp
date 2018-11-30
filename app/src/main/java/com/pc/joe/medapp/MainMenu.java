package com.pc.joe.medapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    User user;
    TextView userTextView;
    Button makeAppointmentButton,viewAppointmentButton, reportButton, setupUserButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userTextView = findViewById(R.id.loginTextView);
        makeAppointmentButton = findViewById(R.id.makeAppointmentButton);
        viewAppointmentButton = findViewById(R.id.viewAppointmentButton);
        reportButton = findViewById(R.id.reportButton);
        setupUserButton = findViewById(R.id.setupButton);
        logoutButton = findViewById(R.id.logoutButton);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User)getIntent().getSerializableExtra("user");
            userTextView.setText("Welcome to MedApp\n"+ user.getUserType()+": "+user.getFirstName()+" "+user.getLastName());

            //Set screen up, use case so it'll be easier to add nonstandard users
            switch(user.getUserType()) {
                case "Admin":
                    //It doesn't make sense to let admin do this
                    makeAppointmentButton.setAlpha(.5f);
                    makeAppointmentButton.setEnabled(false);
                    Log.d("User_Type", "User is an Admin");
                    break;
                default:
                    reportButton.setVisibility(View.INVISIBLE);
                    reportButton.setEnabled(false);
                    setupUserButton.setVisibility(View.INVISIBLE);
                    setupUserButton.setEnabled(false);
                    Log.d("User_Type", "User is not an Admin");
                    break;
            }
        }
        else {
            Log.e("User_Type", "Something went wrong and did not pass in login info");
            finish();
        }

        //schedule
        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appointmentSetupIntent = new Intent(MainMenu.this, ScheduleAppointments.class);
                appointmentSetupIntent.putExtra("user", user);
                MainMenu.this.startActivity(appointmentSetupIntent);
            }
        });

        //
        viewAppointmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent appointmentSetupIntent = new Intent(MainMenu.this, ViewAppointment.class);
                appointmentSetupIntent.putExtra("user", user);
                MainMenu.this.startActivity(appointmentSetupIntent);
            }
        });

        //Setup button
        setupUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userSetupIntent = new Intent(MainMenu.this, userSetup.class);
                userSetupIntent.putExtra("user", user);
                MainMenu.this.startActivity(userSetupIntent);
            }
        });

        //Setup button
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportIntent = new Intent(MainMenu.this, ViewReports.class);
                reportIntent.putExtra("user", user);
                MainMenu.this.startActivity(reportIntent);
            }
        });

        //on clicks
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
