package com.pc.joe.medapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Vars
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    User loginUser;
    Intent MainMenuIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        loginUser = new User();

        //Check Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setEnabled(false);
                usernameEditText.setError(null);
                passwordEditText.setError(null);

                //Prevent blank inputs and null pointers
                if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    usernameEditText.setError("Must be entered");
                    passwordEditText.setError("Must be entered");
                } else {

                    loginUser.setUserName(usernameEditText.getText().toString());
                    loginUser.setPassword(passwordEditText.getText().toString());

                    //Create database connection
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("user/" + loginUser.getUserName());

                    //One time use
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (loginUser.getPassword().equals(dataSnapshot.child("password").getValue().toString())) { //Correct
                                    Log.i("Login", "User logged in successfully: User:" + loginUser.getUserName());

                                    //Set some values about the user from database
                                    loginUser.setUserType(dataSnapshot.child("type").getValue().toString());
                                    loginUser.setFirstName(dataSnapshot.child("firstName").getValue().toString());
                                    loginUser.setLastName(dataSnapshot.child("lastName").getValue().toString());

                                    MainMenuIntent = new Intent(MainActivity.this, MainMenu.class);
                                    MainMenuIntent.putExtra("user", loginUser);
                                    MainActivity.this.startActivity(MainMenuIntent);
                                } else { //Username but not password
                                    Log.i("Login", "User was found, wrong password. User:" + loginUser.getUserName());
                                    passwordEditText.setError("Check password");
                                }
                            } else { //Both wrong
                                Log.i("Login", "User was not found, User: " + loginUser.getUserName());
                                usernameEditText.setError("E-mail was not found");
                                passwordEditText.setError("Incorrect password");
                            }
                        }

                        @Override //Error with database
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Login", "Issue reaching database");
                        }
                    });
                }
                loginButton.setEnabled(true);
            }
        });
    }
}
