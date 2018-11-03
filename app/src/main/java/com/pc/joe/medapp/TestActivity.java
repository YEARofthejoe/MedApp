package com.pc.joe.medapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
public class TestActivity extends AppCompatActivity {

    private ListView listView;
    private AppointmentAdapter appointmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        listView = (ListView) findViewById(R.id.test);
        ArrayList<Appointment> moviesList = new ArrayList<>();
        moviesList.add(new Appointment("Today","Time","Doctor","Patient","Location","Status","Reason"));
        moviesList.add(new Appointment());

        appointmentAdapter = new AppointmentAdapter(this,moviesList);
        listView.setAdapter(appointmentAdapter);

    }
}
