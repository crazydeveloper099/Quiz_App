package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.myapplication.adapters.quizAdapter;
import com.example.myapplication.model_classes.quiz;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class quiz_present extends AppCompatActivity {

     RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    quizAdapter qAdapter;
    ProgressBar pg;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle BundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_present);

        setup();
        populate_recyclerView();




    }
    public void setup()
    {
        recyclerView = findViewById(R.id.rcv);
        pg=findViewById(R.id.pg_bar_quiz_present);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
    }
    public void populate_recyclerView()
    {
        Query quiz_Query =
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("quiz").orderByKey();

        FirebaseRecyclerOptions<quiz> options =
                new FirebaseRecyclerOptions
                        .Builder<quiz>()
                        .setQuery(quiz_Query, quiz.class)
                        .build();
        qAdapter=new quizAdapter(options,pg,recyclerView,quiz_present.this);
        qAdapter.startListening();
        recyclerView.setAdapter(qAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
       // qAdapter.startListening();
        /**pg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);**/
    }

    @Override
    protected void onStop() {
        super.onStop();
       //

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qAdapter.stopListening();
    }
}
