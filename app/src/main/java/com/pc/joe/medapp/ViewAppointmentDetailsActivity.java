package com.pc.joe.medapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAppointmentDetailsActivity extends AppCompatActivity {
    String appointmentDateTime;
    TextView appointmentDateTimeTextView;
    EditText appointmentReasonTextView, appointmentNotesTextView, appointmentPrescriptionTextView;
    Button appointmentCancelButton, appointmentSaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_details);
        appointmentDateTimeTextView = findViewById(R.id.selected_appointment_date_time);
        appointmentReasonTextView = findViewById(R.id.selected_appointment_reason);
        appointmentNotesTextView = findViewById(R.id.selected_appointment_notes);
        appointmentPrescriptionTextView = findViewById(R.id.selected_appointment_prescription);
        appointmentCancelButton = findViewById(R.id.selected_appointment_cancel_button);
        appointmentSaveButton = findViewById(R.id.selected_appointment_save_button);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            appointmentDateTime = extras.getString("SelectedAppointment");
            appointmentDateTimeTextView.setText(appointmentDateTime);

        } else {
            Log.e("MakeApp", "A user was not provided to class");
            finish();
        }

       appointmentCancelButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appointmentTableRef = database.getReference("appointment/");
        appointmentTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //appointmentList = new ArrayList<Appointment>();
                String reason ="", notes="", prescription ="";
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                /*Toast.makeText(getApplicationContext(),"'"+ appointmentDateTime+
                                                "'"+ "'"+dstime.getKey()+"'",
                                        Toast.LENGTH_LONG).show(); */
                                if (dstime.getKey().equals(appointmentDateTime)) {
                                    reason = dstime.child("reason").getValue().toString();
                                    notes = dstime.child("notes").getValue().toString();
                                    prescription =
                                            dstime.child("prescription").getValue().toString();
                                    Toast.makeText(getApplicationContext(), reason,
                                            Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                    }

                    appointmentReasonTextView.setText(reason);
                    appointmentNotesTextView.setText(notes);
                    appointmentPrescriptionTextView.setText(prescription);
                    appointmentReasonTextView.setEnabled(false);
                    appointmentNotesTextView.setEnabled(false);
                    appointmentPrescriptionTextView.setEnabled(false);

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
