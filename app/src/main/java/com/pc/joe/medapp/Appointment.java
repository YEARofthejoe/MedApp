package com.pc.joe.medapp;

public class Appointment {
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentDoctor;
    private String appointmentPatient;
    private String appointmentLocation;
    private String appointmentStatus;
    private String appointmentReason;

    public Appointment(String date, String time, String doctor, String patient, String location, String status, String reason){
        this.appointmentDate = date;
        this.appointmentTime = time;
        this.appointmentDoctor = doctor;
        this.appointmentPatient = patient;
        this.appointmentLocation = location;
        this.appointmentStatus = status;
        this.appointmentReason = reason;
    }
    public Appointment(){
        this.appointmentDate = null;
        this.appointmentTime = null;
        this.appointmentDoctor = null;
        this.appointmentPatient = null;
        this.appointmentLocation = null;
        this.appointmentStatus = null;
        this.appointmentReason = null;
    }


    public void setAppointmentDate(String date){
        this.appointmentDate = date;
    }
    public String getAppointmentDate(){
        return this.appointmentDate;
    }
    public void setAppointmentTime(String time){this.appointmentTime=time;}
    public String getAppointmentTime(){return this.appointmentTime;}
    public void setAppointmentDoctor(String doctor){this.appointmentDoctor=doctor;}
    public String getAppointmentDoctor(){return this.appointmentDoctor;}
    public void setAppointmentPatient(String patient){this.appointmentPatient=patient;}
    public String getAppointmentPatient(){return this.appointmentPatient;}
    public void setAppointmentLocation(String location){this.appointmentLocation=location;}
    public String getAppointmentLocation(){return this.appointmentLocation;}
    public void setAppointmentStatus(String status){this.appointmentStatus=status;}
    public String getAppointmentStatus(){return this.appointmentStatus;}
    public void setAppointmentReason(String reason){this.appointmentReason=reason;}
    public String getAppointmentReason(){return this.appointmentReason;}
}
