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

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder>{

    private ArrayList<Appointment> listOfItems;

    public interface ButtonClickListener{
        void onButtonClick(int position);
    }

    private ButtonClickListener buttonClickListener;
    private Context context;


    public MyListAdapter(Context context, ArrayList<Appointment> listOfItems,
                         ButtonClickListener buttonClickListener) {
        this.context = context;
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Define a constructor taking a View as its parameter
        TextView textToShow;
        Button testButton, testButton2;
        public MyViewHolder(View itemView) {
            super(itemView);
            textToShow = (TextView) itemView.findViewById(R.id.a_date_time);
            testButton = (Button) itemView.findViewById(R.id.button);
            testButton2 = (Button) itemView.findViewById(R.id.button2);
            testButton.setOnClickListener(this);
            testButton2.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(v.getId() == testButton.getId())
                Toast.makeText(v.getContext(), "Button 1 Pressed" +  String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            if(v.getId() == testButton2.getId()) {
               /* Toast.makeText(v.getContext(),
                        "Button 2 Pressed" + String.valueOf(getAdapterPosition()), Toast
                        .LENGTH_SHORT).show(); */
                Intent intent = new Intent(context, ViewAppointmentDetailsActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

}
