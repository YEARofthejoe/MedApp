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
    String appointmentDateTime, userType, user;
    TextView appointmentDateTimeTextView, appointmentUserTextView;
    EditText appointmentReasonTextView, appointmentNotesTextView, appointmentPrescriptionTextView;
    Button appointmentCancelButton, appointmentSaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_details);
        appointmentDateTimeTextView = findViewById(R.id.selected_appointment_date_time);
        appointmentUserTextView = findViewById(R.id.selected_appointment_user);
        appointmentReasonTextView = findViewById(R.id.selected_appointment_reason);
        appointmentNotesTextView = findViewById(R.id.selected_appointment_notes);
        appointmentPrescriptionTextView = findViewById(R.id.selected_appointment_prescription);
        appointmentCancelButton = findViewById(R.id.selected_appointment_cancel_button);
        appointmentSaveButton = findViewById(R.id.selected_appointment_save_button);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass login object
            appointmentDateTime = extras.getString("SelectedAppointment");
            userType = extras.getString("UserType");
            user = extras.getString("User");
            appointmentDateTimeTextView.setText("Appointment Date & Time: "+appointmentDateTime);
            appointmentUserTextView.setText(user);

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
                                /*Toast.makeText(getApplicationContext(),
                                        Boolean.toString(dstime.getKey().equals(appointmentDateTime)),
                                        Toast.LENGTH_LONG).show(); */
                                if (dstime.getKey().equals(appointmentDateTime)) {
                                    reason = dstime.child("reason").getValue().toString();
                                    notes = dstime.child("note").getValue().toString();
                                    prescription =
                                            dstime.child("prescription").getValue().toString();
                                    /*Toast.makeText(getApplicationContext(), reason,
                                            Toast.LENGTH_LONG).show(); */

                                }
                            }

                        }
                    }

                    appointmentReasonTextView.setText(reason);
                    appointmentNotesTextView.setText(notes);
                    appointmentPrescriptionTextView.setText(prescription);
                    appointmentReasonTextView.setEnabled(false);
                    if(userType.equals("Patient")) {
                        appointmentNotesTextView.setEnabled(false);
                        appointmentPrescriptionTextView.setEnabled(false);
                        appointmentSaveButton.setEnabled(false);
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

        appointmentSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveAppointmentInformation();
            }
        });

    }

    public void saveAppointmentInformation(){
        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("appointment/");
        //myRef.child(aLocation).child(aUser).child(aDateTime).child(  "status").setValue("Cancelled");

        //One time use
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dsloc : dataSnapshot.getChildren()) {
                        //if()
                        for (DataSnapshot dsuser : dsloc.getChildren()) {
                            for (DataSnapshot dstime : dsuser.getChildren()) {
                                if (dstime.getKey().equals(appointmentDateTime)) {
                                    /*Toast.makeText(getApplicationContext(), dsloc.getKey(),
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), dsuser.getKey(),
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), dstime.getKey(),
                                            Toast.LENGTH_SHORT).show(); */
                                    myRef.child(dsloc.getKey()).child(dsuser.getKey()).child
                                    (dstime.getKey()).child("note").setValue(appointmentNotesTextView.getText().toString());
                                    myRef.child(dsloc.getKey()).child(dsuser.getKey()).child
                                            (dstime.getKey()).child("prescription").setValue(appointmentPrescriptionTextView.getText().toString());
                                    myRef.child(dsloc.getKey()).child(dsuser.getKey()).child
                                            (dstime.getKey()).child("status").setValue("Completed");
                                    Toast.makeText(getApplicationContext(), "Appointment " +
                                                    "Completed!",
                                            Toast.LENGTH_SHORT).show();


                                }
                            }

                        }
                    }

                } catch (Exception e) {
                    Log.e("Data", "Error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Message", "Something didn't work");
            }
        });
        finish();
        //startActivity(getIntent());
    }
}
