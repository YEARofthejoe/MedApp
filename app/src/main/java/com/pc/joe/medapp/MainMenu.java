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
    Button makeAppointmentButton,viewAppointmentButton, searchRecordsButton, reportButton, setupUserButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userTextView = findViewById(R.id.loginTextView);
        makeAppointmentButton = findViewById(R.id.makeAppointmentButton);
        viewAppointmentButton = findViewById(R.id.viewAppointmentButton);
        searchRecordsButton = findViewById(R.id.seachRecordsButton);
        reportButton = findViewById(R.id.reportButton);
        setupUserButton = findViewById(R.id.setupButton);
        logoutButton = findViewById(R.id.logoutButton);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User)getIntent().getSerializableExtra("user");
            userTextView.setText("Welcome to MedApp\n"+user.getFirstName()+" "+user.getLastName());

            //We log in as User but then pass the data to the correct object type
            switch(user.getUserType()) {
                case "Admin":
                    //It doesn't make sense to let admin do this
                    makeAppointmentButton.setAlpha(.5f);
                    makeAppointmentButton.setClickable(false);
                    break;
                case "Patient":
                    Log.i("User_Type", "User is a patient");
                    break;
                case "Doctor":
                    Log.i("User_Type", "User is a doctor");
                    break;
                case "Receptionist":
                    Log.i("User_Type", "User is a receptionist");
                    break;
                default:
                    Log.i("User_Type", "Something went wrong");
                    finish();
            }
        }
        else {
            Log.e("User_Type", "Something went wrong and did not pass in login info");
            finish();
        }

        setupUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userSetupIntent;

                userSetupIntent = new Intent(MainMenu.this, userSetup.class);
                userSetupIntent.putExtra("user", user);
                MainMenu.this.startActivity(userSetupIntent);
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
