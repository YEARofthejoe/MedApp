package com.pc.joe.medapp;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
        holder.doctor.setText(currAppointment.getAppointmentDoctor());
        holder.location.setText(currAppointment.getAppointmentLocation());
        holder.status.setText(currAppointment.getAppointmentStatus());
        holder.viewButton.setText("View Appointment");
        holder.actionButton.setText("Cancel");
        if(currAppointment.getAppointmentStatus().equals("N")){
            holder.viewButton.setEnabled(false);
        }
        else{
            holder.actionButton.setEnabled(false);
            holder.viewButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Define a constructor taking a View as its parameter
        TextView dateTime, doctor, location, status;
        Button actionButton, viewButton;
        public MyViewHolder(View itemView) {
            super(itemView);
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
            if(v.getId() == actionButton.getId())
                Toast.makeText(v.getContext(),
                        "Action Pressed" +  String.valueOf(getAdapterPosition()),
                        Toast.LENGTH_SHORT).show();
            if(v.getId() == viewButton.getId()) {
                /*Toast.makeText(v.getContext(),dateTime.getText()
                         ,
                        Toast.LENGTH_SHORT).show(); */
                Intent intent = new Intent(context, ViewAppointmentDetailsActivity.class);
                intent.putExtra("SelectedAppointment", dateTime.getText().toString());
                context.startActivity(intent);
                //String.valueOf(getAdapterPosition())
            }
        }

    }
}
