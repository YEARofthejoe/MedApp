package com.pc.joe.medapp;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentAdapter extends ArrayAdapter<Appointment> {
    private Context mContext;
    private List<Appointment> appointmentList = new ArrayList<>();

    public DoctorAppointmentAdapter(Context context, ArrayList<Appointment> list) {
        super(context, 0 , list);
        mContext = context;
        appointmentList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.doctor_appointment,parent,false);

        Appointment currentAppointment = appointmentList.get(position);

        TextView date = (TextView) listItem.findViewById(R.id.appointment_date);
        //TextView time = (TextView) listItem.findViewById(R.id.appointment_time);
        TextView patient = (TextView) listItem.findViewById(R.id.appointment_patient);
        //TextView patient = (TextView) listItem.findViewById(R.id.appointment_patient);
        //TextView location = (TextView) listItem.findViewById(R.id.appointment_location);
        EditText reason = (EditText) listItem.findViewById(R.id.appointment_reason);
        TextView status = (TextView) listItem.findViewById(R.id.appointment_status);
        Button action = (Button) listItem.findViewById(R.id.appointment_action);
        Button view = (Button) listItem.findViewById(R.id.appointment_view);;

        date.setText("Date & Time: "+currentAppointment.getAppointmentDate());
        //time.setText(currentAppointment.getAppointmentTime());
        patient.setText("Patient: "+currentAppointment.getAppointmentPatient());
        //patient.setText(currentAppointment.getAppointmentPatient());
        //location.setText(currentAppointment.getAppointmentLocation());
        reason.setText("Reason: "+currentAppointment.getAppointmentReason());
        reason.setEnabled(false);
        status.setText("Status: "+currentAppointment.getAppointmentStatus());
        view.setText("View Appointment");
        if(currentAppointment.getAppointmentStatus().equals("N")){
            action.setText("Confirm");
            view.setEnabled(false);
        }
        else{
            action.setText("Cancel");
            action.setEnabled(false);
            view.setEnabled(true);
        }

        action.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent appointmentSetupIntent = new Intent(getContext(), TestActivity.class);
                //appointmentSetupIntent.putExtra("user", user);
                //getContext().startActivity(appointmentSetupIntent);
                //Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Appointment Confirmed!",Toast.LENGTH_SHORT).show();

            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent appointmentSetupIntent = new Intent(getContext(), TestActivity.class);
                //appointmentSetupIntent.putExtra("user", user);
                //getContext().startActivity(appointmentSetupIntent);
                Toast.makeText(getContext(),"View appointment clicked",Toast.LENGTH_SHORT).show();
            }
        });
        return listItem;
    }
}
