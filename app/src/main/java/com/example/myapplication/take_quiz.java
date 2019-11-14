package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import mehdi.sakout.fancybuttons.FancyButton;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

public class take_quiz extends AppCompatActivity {
    FancyButton next;
    RadioGroup rg;
    TextView question_tv,op1,op2,op3,op4,score;
    String topic,email;

    int choice=0,correct_choice,max_score,question_score=0,question_number=1,total_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);
        getBundle();
        setup();
        fill_data("question_1");
        onclickListeners();
    }
    public void getBundle()
    {
        Intent intent = this.getIntent();
        if(intent !=null)
        {
            topic = intent.getExtras().getString("topic");
            email = intent.getExtras().getString("email");
            total_score= Integer.valueOf(intent.getExtras().getString("max_score"));
        }
        else
        {
            topic=null;
        }
    }
    public void setup()
    {
        next=findViewById(R.id.btn_next);
        rg=findViewById(R.id.radio_group);
        question_tv=findViewById(R.id.question_take_quiz);
        op1=findViewById(R.id.option1);
        op2=findViewById(R.id.option2);
        op3=findViewById(R.id.option3);
        op4=findViewById(R.id.option4);
        score=findViewById(R.id.score);
        TextView topic_tv=findViewById(R.id.topic);
        topic_tv.setText(topic);
        String text="Score: 0";
        score.setText(text);

    }
    public void fill_data(final String question)
    {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("quiz")
                .child(topic)
                .child("quizes")
                .child(email)
                .child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(question).exists()) {
                            question_tv.setText(dataSnapshot.child(question).child("question").getValue().toString());
                            op1.setText(dataSnapshot.child(question).child("op1").getValue().toString());
                            op2.setText(dataSnapshot.child(question).child("op2").getValue().toString());
                            op3.setText(dataSnapshot.child(question).child("op3").getValue().toString());
                            op4.setText(dataSnapshot.child(question).child("op4").getValue().toString());
                            correct_choice = Integer.valueOf(dataSnapshot.child(question).child("choice").getValue().toString());
                            max_score = Integer.valueOf(dataSnapshot.child(question).child("max_score").getValue().toString());
                        }
                        else
                        {
                            new MaDialog.Builder(take_quiz.this)
                                    .setTitle("Score")
                                    .setMessage("Your Score "+question_score+"/"+total_score)
                                    .setPositiveButtonText("ok")
                                    .setCancelableOnOutsideTouch(false)
                                    .setPositiveButtonListener(new MaDialogListener() {
                                        @Override
                                        public void onClick() {
                                            take_quiz.this.finish();
                                        }
                                    })
                                    .build();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public void onclickListeners()
    {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.r_op1) choice=1;
                else if(checkedId == R.id.r_op2)choice=2;
                else if(checkedId == R.id.r_op3)choice=3;
                else if(checkedId == R.id.r_op4)choice=4;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(choice!=0)
            {
                if (choice == correct_choice) {
                    question_score = question_score + max_score;
                    String text = "Score: " + question_score;
                    score.setText(text);
                    question_number += 1;
                    fill_data("question_" + question_number);

                }
                else {
                    question_number += 1;
                    fill_data("question_" + question_number);

                }
            }
            else {
                Toast.makeText(take_quiz.this, "Please choose an option", Toast.LENGTH_SHORT).show();
            }
            }


        });

    }


}
