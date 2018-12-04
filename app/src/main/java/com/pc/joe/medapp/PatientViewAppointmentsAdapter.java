package com.pc.joe.medapp;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientViewAppointmentsAdapter extends RecyclerView.Adapter<PatientViewAppointmentsAdapter.MyViewHolder>{
    private ArrayList<Appointment> appointments;

    public interface ButtonClickListener{
        void onButtonClick(int position);
    }

    private ButtonClickListener buttonClickListener;
    private Context context;


    public PatientViewAppointmentsAdapter(Context context, ArrayList<Appointment> appointments,
                         ButtonClickListener buttonClickListener) {
        this.context = context;
        this.appointments = appointments;
        this.buttonClickListener = buttonClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Boolean attachViewImmediatelyToParent = false;
        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appointment,
                parent,attachViewImmediatelyToParent);
        PatientViewAppointmentsAdapter.MyViewHolder myViewHolder = new PatientViewAppointmentsAdapter.MyViewHolder(singleItemLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(PatientViewAppointmentsAdapter.MyViewHolder holder, int position) {
        Appointment currAppointment = appointments.get(position);
        holder.dateTime.setText(currAppointment.getAppointmentDate());
        if(currAppointment.getAppointmentTime().equals(currAppointment.getAppointmentDoctor())) {
            holder.doctor.setText(currAppointment.getAppointmentPatient());
            holder.user.setText("doctor");
        }
        else {
            holder.doctor.setText(currAppointment.getAppointmentDoctor());
            holder.user.setText("patient");
        }

        holder.location.setText(currAppointment.getAppointmentLocation());
        holder.status.setText(currAppointment.getAppointmentStatus());
        //holder.user.setText(curr);
        holder.viewButton.setText("View Appointment");
        holder.actionButton.setText("Cancel");

        if(currAppointment.getAppointmentStatus().equals("N") && holder.user.getText().equals(
                "patient")){
            holder.viewButton.setEnabled(false);
        }
        else if(currAppointment.getAppointmentStatus().equals("Completed")||currAppointment.getAppointmentStatus().equals("Cancelled") && holder.user.getText().equals(
                "patient")){
            holder.actionButton.setEnabled(false);
            holder.viewButton.setEnabled(true);
        }

        if(currAppointment.getAppointmentStatus().equals("Completed")||currAppointment.getAppointmentStatus().equals("Cancelled") && holder.user.getText().equals(
                "doctor")){
            holder.actionButton.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void refreshEvents(ArrayList<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Define a constructor taking a View as its parameter
        public TextView dateTime, doctor, location, status, user;
        Button actionButton, viewButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.username);
            dateTime = (TextView) itemView.findViewById(R.id.appointment_date);
            doctor = (TextView) itemView.findViewById(R.id.appointment_doctor);
            location = (TextView) itemView.findViewById(R.id.appointment_location);
            status = (TextView) itemView.findViewById(R.id.appointment_status);
            actionButton = (Button) itemView.findViewById(R.id.appointment_action);
            viewButton = (Button) itemView.findViewById(R.id.appointment_view);
            actionButton.setOnClickListener(this);
            viewButton.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(v.getId() == actionButton.getId()) {
                /*Toast.makeText(v.getContext(),
                        "Action Pressed" +  String.valueOf(getAdapterPosition()),
                        Toast.LENGTH_SHORT).show(); */
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
                                        if (dstime.getKey().equals(dateTime.getText().toString())) {
                                            /*Toast.makeText(context, dsloc.getKey(),
                                                    Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, dsuser.getKey(),
                                                    Toast.LENGTH_SHORT).show(); */

                                        myRef.child(dsloc.getKey()).child(dsuser.getKey()).child
                                                (dstime.getKey()).child("status").setValue
                                                ("Cancelled");
                                            Toast.makeText(context, "Appointment Cancelled!",
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
            }
            if(v.getId() == viewButton.getId()) {
                /*Toast.makeText(v.getContext(),dateTime.getText()
                         ,
                        Toast.LENGTH_SHORT).show(); */

                Intent intent = new Intent(context, ViewAppointmentDetailsActivity.class);
                intent.putExtra("SelectedAppointment", dateTime.getText().toString());
                intent.putExtra("UserType",user.getText().toString());
                intent.putExtra("User",doctor.getText().toString());

                context.startActivity(intent);
                //String.valueOf(getAdapterPosition())
            }
        }

    }

}
