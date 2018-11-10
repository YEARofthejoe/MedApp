package com.pc.joe.medapp;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment>{
    private Context mContext;
    private List<Appointment> appointmentList = new ArrayList<>();

    public AppointmentAdapter(Context context, ArrayList<Appointment> list) {
        super(context, 0 , list);
        mContext = context;
        appointmentList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.patient_appointment,parent,false);

        Appointment currentAppointment = appointmentList.get(position);

        TextView date = (TextView) listItem.findViewById(R.id.appointment_date);
        //TextView time = (TextView) listItem.findViewById(R.id.appointment_time);
        TextView doctor = (TextView) listItem.findViewById(R.id.appointment_doctor);
        //TextView patient = (TextView) listItem.findViewById(R.id.appointment_patient);
        TextView location = (TextView) listItem.findViewById(R.id.appointment_location);
        //EditText reason = (EditText) listItem.findViewById(R.id.appointment_reason);
        TextView status = (TextView) listItem.findViewById(R.id.appointment_status);
        Button action = (Button) listItem.findViewById(R.id.appointment_action);
        Button view = (Button) listItem.findViewById(R.id.appointment_view);;

        date.setText("Date & Time: "+currentAppointment.getAppointmentDate());
        //time.setText(currentAppointment.getAppointmentTime());
        doctor.setText("Doctor: "+currentAppointment.getAppointmentDoctor());
        //patient.setText(currentAppointment.getAppointmentPatient());
        location.setText("Location: "+currentAppointment.getAppointmentLocation());
        //reason.setText(currentAppointment.getAppointmentReason());
        status.setText("Status: "+currentAppointment.getAppointmentStatus());
        view.setText("View Appointment");
        if(currentAppointment.getAppointmentStatus().equals("N")){
            action.setText("Cancel");
            view.setEnabled(false);
        }
        else{
            action.setText("Completed");
            action.setEnabled(false);
            view.setEnabled(true);
        }

        action.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent appointmentSetupIntent = new Intent(getContext(), TestActivity.class);
                //appointmentSetupIntent.putExtra("user", user);
                //getContext().startActivity(appointmentSetupIntent);
                Toast.makeText(getContext(),"Appointment Cancelled!",Toast.LENGTH_SHORT).show();
            }
        });
        return listItem;
    }



}
