package com.pc.joe.medapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private Context mContext;
    private List<Appointment> appointmentList = new ArrayList<>();
    String appointmentDateTime, aLocation, aUser;

    public AppointmentAdapter(Context context, ArrayList<Appointment> list) {
        super(context, 0, list);
        mContext = context;
        appointmentList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.patient_appointment, parent, false);

        Appointment currentAppointment = appointmentList.get(position);

        TextView dateTime = (TextView) listItem.findViewById(R.id.appointment_date);
        //TextView time = (TextView) listItem.findViewById(R.id.appointment_time);
        TextView doctor = (TextView) listItem.findViewById(R.id.appointment_doctor);
        //TextView patient = (TextView) listItem.findViewById(R.id.appointment_patient);
        TextView location = (TextView) listItem.findViewById(R.id.appointment_location);
        //EditText reason = (EditText) listItem.findViewById(R.id.appointment_reason);
        TextView status = (TextView) listItem.findViewById(R.id.appointment_status);
        Button action = (Button) listItem.findViewById(R.id.appointment_action);
        Button view = (Button) listItem.findViewById(R.id.appointment_view);

        dateTime.setText("Date & Time: " + currentAppointment.getAppointmentDate());
        //time.setText(currentAppointment.getAppointmentTime());
        doctor.setText("Doctor: " + currentAppointment.getAppointmentDoctor());
        //patient.setText(currentAppointment.getAppointmentPatient());
        location.setText("Location: " + currentAppointment.getAppointmentLocation());
        //reason.setText(currentAppointment.getAppointmentReason());
        status.setText("Status: " + currentAppointment.getAppointmentStatus());
        view.setText("View Appointment");
        if (currentAppointment.getAppointmentStatus().equals("N")) {
            action.setText("Cancel");
            view.setEnabled(false);
        } else {
            action.setText("Completed");
            action.setEnabled(false);
            view.setEnabled(true);
        }
        appointmentDateTime = currentAppointment.getAppointmentDate();
        aLocation = currentAppointment.getAppointmentLocation();
        aUser = currentAppointment.getAppointmentPatient();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent appointmentSetupIntent = new Intent(getContext(), TestActivity.class);
                //appointmentSetupIntent.putExtra("user", user);
                //getContext().startActivity(appointmentSetupIntent);
                cancelPatientAppointment(appointmentDateTime, aLocation, aUser);
            }
        });
        return listItem;
    }

    private void cancelPatientAppointment(final String aDateTime, String aLocation,
                                          String aUser) {
        Toast.makeText(getContext(), aLocation, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), dsloc.getKey(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), dsuser.getKey(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), dstime.getKey(), Toast.LENGTH_SHORT).show();
                                    //myRef.child(dsloc.getKey()).child(dsuser.getKey()).child
                                    // (dstime.getKey()).child(  "status").setValue("Cancelled");

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
    }


}
