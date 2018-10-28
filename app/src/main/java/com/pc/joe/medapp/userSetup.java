package com.pc.joe.medapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userSetup extends AppCompatActivity {

    Boolean okToInsert, allFields;
    User passedUser;
    Admin adminUser;
    Button submitButton;
    EditText firstNameEditText, lastNameEditText, usernameEditText, passwordEditText, departmentEditText, specialtyEditText, locationEditText;
    TextView departmentTextView, specTextView, locTextView;
    Spinner userTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        //setup up those widgets
        submitButton = findViewById(R.id.submitUserButton);

        firstNameEditText = findViewById(R.id.setupFirstNameEditText);
        lastNameEditText = findViewById(R.id.setupLastNameEditText);
        usernameEditText = findViewById(R.id.setupUsernameEditText);
        passwordEditText = findViewById(R.id.setupPasswordEditText);
        departmentEditText = findViewById(R.id.setupDepartmentEditText);
        specialtyEditText = findViewById(R.id.setupSpecialtyEditText);
        locationEditText = findViewById(R.id.setupLocationEditText);

        departmentTextView = findViewById(R.id.setupDepartmentText);
        specTextView = findViewById(R.id.setupSpecText);
        locTextView = findViewById(R.id.setupLocText);

        userTypeSpinner = findViewById(R.id.setupUserTypeSpinner);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Pass user
            passedUser = (User) getIntent().getSerializableExtra("user");
            Log.d("User_Setup", "The person using this screen is "+passedUser.getUserName());

            adminUser = new Admin();

            //Create Admin User Object
            adminUser.setUserName(passedUser.getUserName());

        }
        else{
            Log.e("User_Setup", "Something went wrong and did not pass in user info");
            finish();
        }

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //We want to hide some stuff if it isn't right for that user type
                String selectedString = userTypeSpinner.getSelectedItem().toString();
                if(selectedString.equals("Patient") || selectedString.equals("Admin")) {
                    departmentEditText.setVisibility(View.INVISIBLE);
                    specialtyEditText.setVisibility(View.INVISIBLE);
                    specialtyEditText.setVisibility(View.INVISIBLE);
                    locationEditText.setVisibility(View.INVISIBLE);

                    departmentTextView.setVisibility(View.INVISIBLE);
                    specTextView.setVisibility(View.INVISIBLE);
                    locTextView.setVisibility(View.INVISIBLE);

                    allFields = false;
                }
                else{
                    departmentEditText.setVisibility(View.VISIBLE);
                    specialtyEditText.setVisibility(View.VISIBLE);
                    specialtyEditText.setVisibility(View.VISIBLE);
                    locationEditText.setVisibility(View.VISIBLE);

                    departmentTextView.setVisibility(View.VISIBLE);
                    specTextView.setVisibility(View.VISIBLE);
                    locTextView.setVisibility(View.VISIBLE);

                    allFields = true;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitButton.setClickable(false);
                //we cant fail at all. Lets assume we didn't fail until we do
                okToInsert = true;
                //We need to clear the errors encase they mess up a second run..
                firstNameEditText.setError(null);
                lastNameEditText.setError(null);
                usernameEditText.setError(null);
                passwordEditText.setError(null);
                departmentEditText.setError(null);
                specialtyEditText.setError(null);
                locationEditText.setError(null);

                //check fields, error out the ones that are blank or whatever.
                if(firstNameEditText.getText().toString().equals("")){
                    firstNameEditText.setError("You must enter a first name");
                    okToInsert = false;
                }
                if(lastNameEditText.getText().toString().equals("")){
                    lastNameEditText.setError("You must enter a last name");
                    okToInsert = false;
                }
                if(usernameEditText.getText().toString().equals("")){
                    usernameEditText.setError("You must enter a valid email address");
                    okToInsert = false;
                }
                if(passwordEditText.getText().toString().equals("")){
                    passwordEditText.setError("You must enter a password");
                    okToInsert = false;
                }
                //we can skip these if we are a patient/admin, they will be hidden
                if(allFields) {
                    if(departmentEditText.getText().toString().equals("")) {
                        departmentEditText.setError("You must enter a department");
                        okToInsert = false;
                    }
                    if(specialtyEditText.getText().toString().equals("")) {
                        specialtyEditText.setError("You must enter a specialty");
                        okToInsert = false;
                    }
                    if(locationEditText.getText().toString().equals("")){
                        locationEditText.setError("You must enter a location");
                        okToInsert = false;
                    }
                }

                //Since all the fields are good lets check if that user already exists.
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("user/");

                //One time use
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check if already there
                        if (dataSnapshot.hasChild(usernameEditText.getText().toString())){
                            //we might of been blank and cancel our actual error
                            if(okToInsert = false) {
                                usernameEditText.setError("This user already exists");
                            }
                            okToInsert = false;
                        }

                        //lets add them, if we got there that user doesn't exit
                        if(okToInsert == true) {
                            Log.d("Database", "We checked that database, we can insert so lets insert");
                            myRef.child(usernameEditText.getText().toString()).child("firstName").setValue(firstNameEditText.getText().toString());
                            myRef.child(usernameEditText.getText().toString()).child("lastName").setValue(lastNameEditText.getText().toString());
                            myRef.child(usernameEditText.getText().toString()).child("password").setValue(passwordEditText.getText().toString());
                            myRef.child(usernameEditText.getText().toString()).child("type").setValue(userTypeSpinner.getSelectedItem().toString());

                            if(allFields){
                                myRef.child(usernameEditText.getText().toString()).child("department").setValue(departmentEditText.getText().toString());
                                myRef.child(usernameEditText.getText().toString()).child("specialty").setValue(specialtyEditText.getText().toString());
                                myRef.child(usernameEditText.getText().toString()).child("location").setValue(locationEditText.getText().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("Message", "Something didn't work");
                    }
                });

                if(okToInsert) {
                    Toast.makeText(getApplicationContext(), "User has been created", Toast.LENGTH_LONG).show();
                    finish();
                }
                submitButton.setClickable(true);
            }
        });

    }
}
