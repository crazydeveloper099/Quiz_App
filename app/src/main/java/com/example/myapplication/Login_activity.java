package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;

public class Login_activity extends AppCompatActivity {

    EditText email,password;
    FancyButton login,signup;
    ProgressBar pg;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        login_check();
        setup();
        button_click_listeners();
        restore_instance_state(savedInstanceState);

    }
    public void setup()
    {
        signup=findViewById(R.id.btn_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        pg=findViewById(R.id.pg_bar);
        login=findViewById(R.id.btn_login);

    }
    public void button_click_listeners()
    {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_activity.this,Signup_activity.class));
                Login_activity.this.finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_method();
            }
        });

    }
    public void restore_instance_state(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            String email_text= savedInstanceState.getString("email");
            String pass_text= savedInstanceState.getString("password");
            email.setText(email_text);
            password.setText(pass_text);

        }

    }
    public void login_check()
    {
            authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =
                        firebaseAuth.getCurrentUser();

                if (user != null) {

                    Intent I = new Intent(Login_activity.this, MainActivity.class);
                    startActivity(I);
                    Login_activity.this.finish();

                } else {
                    Toast.makeText(Login_activity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void login_method()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getText().toString();
                String userPaswd = password.getText().toString();

                if (userEmail.isEmpty()) {
                    email.setError("Provide your Email first!");
                    email.requestFocus();
                }
                else if (userPaswd.isEmpty()) {
                    password.setError("Enter Password!");
                    password.requestFocus();
                }
                else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(Login_activity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    email.setEnabled(false);
                    password.setEnabled(false);
                    login.setEnabled(false);
                    signup.setEnabled(false);
                    pg.setVisibility(View.VISIBLE);

                    login(userEmail,userPaswd);

                } else {
                    Toast.makeText(Login_activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void login(String userEmail,String userPaswd)
    {
        firebaseAuth
                .signInWithEmailAndPassword(userEmail, userPaswd)
                .addOnCompleteListener(Login_activity.this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(Login_activity.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                    pg.setVisibility(View.GONE);
                    email.setEnabled(true);
                    password.setEnabled(true);
                    login.setEnabled(true);
                    signup.setEnabled(true);

                } else {

                    startActivity(new Intent(Login_activity.this, MainActivity.class));
                    pg.setVisibility(View.GONE);
                    Login_activity.this.finish();
                }
            }


        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email",email.getText().toString());
        outState.putString("password",password.getText().toString());

    }
}




