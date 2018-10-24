package com.pc.joe.medapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    User user;
    Toast yummyToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            user = (User)getIntent().getSerializableExtra("user");
        }

    }
}
