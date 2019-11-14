package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {
        FancyButton add_quiz,take_quiz,my_quiz,logout;
        FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
        button_click_listeners();

    }
    public void setup()
    {
        add_quiz=findViewById(R.id.add_quiz);
        take_quiz=findViewById(R.id.take_quiz);
        my_quiz=findViewById(R.id.my_quiz);
        logout=findViewById(R.id.btn_logout);
        auth=FirebaseAuth.getInstance();
    }

    public void button_click_listeners()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this,Login_activity.class));
                MainActivity.this.finish();
            }
        });
        my_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Will be available soon..", Toast.LENGTH_SHORT).show();
            }
        });
        add_quiz.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,Add_quiz.class));

             }
        });
        take_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,quiz_present.class));
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
    }
}
