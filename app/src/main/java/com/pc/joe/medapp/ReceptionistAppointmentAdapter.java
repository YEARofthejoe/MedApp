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
public class ReceptionistAppointmentAdapter extends ArrayAdapter<Appointment>{
    private Context mContext;
    private List<Appointment> appointmentList = new ArrayList<>();

    public ReceptionistAppointmentAdapter(Context context, ArrayList<Appointment> list) {
        super(context, 0 , list);
        mContext = context;
        appointmentList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.receptionist_appointment,parent,false);

        Appointment currentAppointment = appointmentList.get(position);

        TextView date = (TextView) listItem.findViewById(R.id.appointment_date_time);
        TextView doctor = (TextView) listItem.findViewById(R.id.appointment_doctor);
        TextView patient = (TextView) listItem.findViewById(R.id.appointment_patient);
        TextView status = (TextView) listItem.findViewById(R.id.appointment_status);
        Button action = (Button) listItem.findViewById(R.id.appointment_action);

        date.setText("Date & Time: "+currentAppointment.getAppointmentDate());
        doctor.setText("Doctor: "+currentAppointment.getAppointmentDoctor());
        patient.setText("Patient: "+currentAppointment.getAppointmentPatient());
        status.setText("Status: "+currentAppointment.getAppointmentStatus());
        if(currentAppointment.getAppointmentStatus().equals("N")){
            action.setText("Confirm");
        }
        else{
            action.setText("Cancel");
            action.setEnabled(false);
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
        return listItem;
    }

}
