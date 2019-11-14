package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.adapters.topicAdapter;
import com.example.myapplication.model_classes.topic_specific_quiz_model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Topic_specific_quiz extends AppCompatActivity {

    String topic;
    RecyclerView recyclerView;
    topicAdapter adapter;
    ProgressBar progressBar;
    LinearLayout data,no_data;
    TextView textView_heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_specific_quiz);

        getIntentData();
        data_checker();
        setup();
        populate_recyclerView();
    }
    public void getIntentData()
    {
        Intent intent = this.getIntent();
        if(intent !=null)
        {
            topic = intent.getExtras().getString("topic");

        }
        else
        {
            topic=null;
        }
    }

    public void data_checker()
    {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("quiz")
                .child(topic)
                .child("quizes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren())
                {

                    data.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);

                }
                else
                {
                    data.setVisibility(View.INVISIBLE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setup()
    {
        textView_heading=findViewById(R.id.topic_selected);
        textView_heading.setText(topic);
        recyclerView = findViewById(R.id.rcv);
        progressBar=findViewById(R.id.pg_bar_quiz_present);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        data=findViewById(R.id.data);
        no_data=findViewById(R.id.no_data);
    }
    public void populate_recyclerView() {

        if (topic != null) {
            data.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);
            Query quiz_Query =
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("quiz")
                            .child(topic)
                            .child("quizes")
                            .orderByKey();

            FirebaseRecyclerOptions<topic_specific_quiz_model> options =
                    new FirebaseRecyclerOptions
                            .Builder<topic_specific_quiz_model>()
                            .setQuery(quiz_Query, topic_specific_quiz_model.class)
                            .build();
            adapter = new topicAdapter(options, progressBar, recyclerView, Topic_specific_quiz.this, topic);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
