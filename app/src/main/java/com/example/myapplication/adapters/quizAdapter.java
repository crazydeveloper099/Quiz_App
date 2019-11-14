package com.example.myapplication.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Topic_specific_quiz;
import com.example.myapplication.model_classes.quiz;
import com.example.myapplication.quiz_present;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class quizAdapter extends FirebaseRecyclerAdapter<quiz, quizAdapter.Vh> {
    ProgressBar pg;RecyclerView rv; quiz_present context;
    public quizAdapter(@NonNull FirebaseRecyclerOptions<quiz> options,ProgressBar pg,RecyclerView rv, quiz_present qp) {
        super(options);
        this.pg=pg;
        this.rv=rv;
        context=qp;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        pg.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Vh vh, int i, @NonNull final quiz model) {
        vh.topic.setText(model.getTopic());
        vh.players.setText(model.getPlayers());
        FirebaseDatabase.getInstance().getReference().child("quiz").child(model.getTopic()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("quizes").exists()) {
                    String text = dataSnapshot.child("quizes").getChildrenCount() + " active quizes";
                    vh.count.setText(text);
                }
                else
                {
                    String text = "0 active quizes";
                    vh.count.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("quiz").child(model.getTopic()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("players").exists()) {
                    String text = String.valueOf(dataSnapshot.child("players").getChildrenCount());
                    vh.players.setText(text);
                }
                else
                {
                    String text = "0 ";
                    vh.players.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        vh.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, Topic_specific_quiz.class);
                i.putExtra("topic",model.getTopic());
                context.startActivity(i);
            }
        });


    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.quizes_present_row, parent, false);

        return new Vh(view);
    }

    public class Vh extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView topic,players,count;
        public Vh(@NonNull View itemView) {
            super(itemView);
            topic=itemView.findViewById(R.id.title_row);
            players=itemView.findViewById(R.id.players);
            count=itemView.findViewById(R.id.number_of_quizes);
            cv=itemView.findViewById(R.id.card);

        }
    }
}
