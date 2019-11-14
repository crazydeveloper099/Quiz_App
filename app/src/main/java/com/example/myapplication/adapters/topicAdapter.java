package com.example.myapplication.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Topic_specific_quiz;
import com.example.myapplication.model_classes.topic_specific_quiz_model;
import com.example.myapplication.take_quiz;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class topicAdapter extends FirebaseRecyclerAdapter<topic_specific_quiz_model, topicAdapter.Vh> {
    private ProgressBar pg;
    private RecyclerView rv;
    private Topic_specific_quiz context;
    private String topic;
    String email;
    public topicAdapter(@NonNull FirebaseRecyclerOptions<topic_specific_quiz_model> options,ProgressBar pg,RecyclerView rv, Topic_specific_quiz qp, String topic) {
        super(options);
        this.pg=pg;
        this.rv=rv;
        context=qp;
        this.topic=topic;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        pg.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }



    @Override
    protected void onBindViewHolder(@NonNull Vh vh, int i, @NonNull topic_specific_quiz_model model) {
        vh.creator.setText(model.getCreator());
        email=model.getEmail().replace(".","_");
        setQuestionCount_and_score(vh);
        setProfileImage(vh,model);



    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.topic_specific_quiz_item, parent, false);

        return new Vh(view);
    }

    public class Vh extends RecyclerView.ViewHolder
    {
        ImageButton play_bt;
        TextView questions_count,creator,score;
        CircularImageView profile_picture;
        ProgressBar pg_bar_image;

        public Vh(@NonNull View itemView) {
            super(itemView);
            play_bt=itemView.findViewById(R.id.play_button);
            questions_count=itemView.findViewById(R.id.total_questions);
            creator=itemView.findViewById(R.id.creator_topic);
            score=itemView.findViewById(R.id.score);
            profile_picture=itemView.findViewById(R.id.profile_picture);
            pg_bar_image=itemView.findViewById(R.id.pg_bar_image);
            play_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, take_quiz.class);
                    i.putExtra("topic",topic);
                    i.putExtra("email",email);
                    i.putExtra("max_score",score.getText().toString());
                    context.startActivity(i);

                }
            });

        }
    }

    private void setQuestionCount_and_score(final Vh vh)
    {

        FirebaseDatabase.getInstance()
                .getReference()
                .child("quiz")
                .child(topic)
                .child("quizes")
                .child(email)
                .child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String text="Questions: "+dataSnapshot.getChildrenCount();
                        vh.questions_count.setText(text);
                        if(dataSnapshot.getChildrenCount()==0)
                        {
                            vh.play_bt.setEnabled(false);
                        }
                        int score=0;
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            score=score+Integer.parseInt(ds.child("max_score").getValue().toString());
                        }
                        vh.score.setText(String.valueOf(score));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setProfileImage(final Vh vh, topic_specific_quiz_model model)
    {
        String path="profile_images/"+model.getEmail();
        FirebaseStorage.getInstance()
                .getReference()
                .child(path)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(vh.profile_picture,new Callback() {
                    @Override
                    public void onSuccess() {
                        vh.pg_bar_image.setVisibility(View.GONE);
                        vh.profile_picture.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, "Error in loading image.", Toast.LENGTH_SHORT).show();
                        vh.pg_bar_image.setVisibility(View.GONE);
                        vh.profile_picture.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Error:"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
