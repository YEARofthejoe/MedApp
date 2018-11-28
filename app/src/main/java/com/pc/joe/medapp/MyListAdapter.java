package com.pc.joe.medapp;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;


import java.util.List;
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder>{

    private ArrayList<Appointment> listOfItems;

    public interface ButtonClickListener{
        void onButtonClick(int position);
    }

    ButtonClickListener buttonClickListener;


    public MyListAdapter(ArrayList<Appointment> listOfItems) {
        this.listOfItems = listOfItems;
        this.buttonClickListener = buttonClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Boolean attachViewImmediatelyToParent = false;
        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout,
                parent,attachViewImmediatelyToParent);
        MyViewHolder myViewHolder = new MyViewHolder(singleItemLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyListAdapter.MyViewHolder holder, int position) {
        Appointment currAppointment = listOfItems.get(position);
        holder.textToShow.setText(currAppointment.getAppointmentDate());
        holder.testButton.setText(currAppointment.getAppointmentStatus());
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        //Define a constructor taking a View as its parameter
        TextView textToShow;
        Button testButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            textToShow = (TextView) itemView.findViewById(R.id.a_date_time);
            testButton = (Button) itemView.findViewById(R.id.button);
            //testButton.setOnClickListener(this);
        }

    }

}
