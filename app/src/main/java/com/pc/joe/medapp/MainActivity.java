package com.pc.joe.medapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
    Toast yummyToast;

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
                loginUser.setUserName(usernameEditText.getText().toString());
                loginUser.setPassword(passwordEditText.getText().toString());

                //Create database connection
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("user/"+loginUser.getUserName());

                //One time use
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            if(loginUser.getPassword().equals(dataSnapshot.child("password").getValue().toString())){ //Correct
                                Log.i("Login", "User logged in successfully: User:"+loginUser.getUserName());

                                loginUser.setUserType(dataSnapshot.child("type").getValue().toString());

                                MainMenuIntent = new Intent(MainActivity.this, MainMenu.class);
                                MainMenuIntent.putExtra("user", loginUser);
                                MainActivity.this.startActivity(MainMenuIntent);
                            }
                            else{ //Username but not password
                                Log.i("Login", "User was found, wrong password. User:"+loginUser.getUserName());
                                yummyToast = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG);
                                yummyToast.show();
                            }
                        }
                        else{ //Both wrong
                            Log.i("Login", "User was not found, User: "+loginUser.getUserName());
                            yummyToast = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG);
                            yummyToast.show();
                        }
                    }
                    @Override //Error with database
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Login", "Issue reaching database");
                    }
                });
            }
        });
    }
}
