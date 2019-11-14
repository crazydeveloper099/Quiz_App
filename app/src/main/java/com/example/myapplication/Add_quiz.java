package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.adapters.questionsAdapter;
import com.example.myapplication.model_classes.firebase_model;
import com.example.myapplication.model_classes.question_model;
import com.example.myapplication.model_classes.topic_model_firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class Add_quiz extends AppCompatActivity {
    RecyclerView mRecyclerView;
    questionsAdapter madapter;
    EditText title, creator;
    RecyclerView.LayoutManager manager;
    FancyButton add;
    ArrayList<question_model> arrayList;
    private DatabaseReference mDatabase;
    FirebaseAuth auth;
    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        initializer(savedInstanceState);
        add_question();
        remove_question();
        upload_question();


    }


    public void initializer(Bundle savedInstanceState) {

        //binding views
        add = findViewById(R.id.btn_add_question);
        arrayList = new ArrayList<>();
        title = findViewById(R.id.title_quiz);
        creator = findViewById(R.id.creator_quiz);
        mRecyclerView = findViewById(R.id.question_list);
        mRecyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user_email=auth.getCurrentUser().getEmail().replace(".","_");

        //Initializing recyclerview and adapter
        //if data is saved in bundle
        if (savedInstanceState != null) {
            arrayList = savedInstanceState.getParcelableArrayList("arr");
            madapter = new questionsAdapter(arrayList);
            manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(madapter);

        //if its a new activity created
        } else {
            madapter = new questionsAdapter(arrayList);
            manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(madapter);
        }
    }

    //triggers when add button is clicked
    public void add_question() {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.isEmpty()) {
                    arrayList.add(new question_model("", "", "", "", "", -1,0));
                    madapter.notifyDataSetChanged();
                } else {
                    arrayList.add(new question_model("", "", "", "", "",-1 ,0));
                    madapter.notifyItemInserted(arrayList.size());
                }
            }
        });
    }

    public void remove_question () {
        madapter.setOnItemClickListener(new questionsAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int pos, ProgressBar pg, FancyButton remove_bt) {
                remove_from_firebase(pos, pg, remove_bt);


            }
        });
    }



    //triggers when upload button is pressed
    public void upload_question() {

        madapter.setupload(new questionsAdapter.upload() {

            @Override
            public void uload_pressed(final String q, final String o1, final String o2,
                                      final String o3, final String o4,int max_score, final int choice,
                                      final int pos, final ProgressBar pg, final FancyButton upload) {

                            if (!(q.isEmpty() && o1.isEmpty() &&
                                o2.isEmpty() && o3.isEmpty() &&
                                o4.isEmpty())&& max_score!=-1 && choice != 0) {

                                         if (!(title.getText().toString().isEmpty() &&
                                                 creator.getText().toString().isEmpty())) {

                                                    add_to_firebase_quiz(q,o1,o2,o3,o4,max_score,choice,pos,pg,upload);
                                                    add_to_firebase_user(q,o1,o2,o3,o4,max_score,choice,pos);


                                        } else {
                                        Toast.makeText(Add_quiz.this, "Please fill title and creator name.", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(Add_quiz.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

    //To remove question from quiz and user root from firebase
    public void remove_from_firebase(final int pos, final ProgressBar pg, final FancyButton remove_bt)
    {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!title.getText().toString().isEmpty()) {
                    String question_no = "question_" + (pos + 1);

                    //removing question from quiz root
                    if ( dataSnapshot.child("quiz")
                            .child(title.getText().toString())
                            .child("quizes")
                            .child(user_email)
                            .child("questions")
                            .child(question_no)
                            .exists())
                        {

                            pg.setVisibility(View.VISIBLE);
                            remove_bt.setEnabled(false);

                            dataSnapshot.child("quiz")
                                    .child(title.getText().toString())
                                    .child(user_email)
                                    .child("questions")
                                    .child(question_no)
                                    .getRef().removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Add_quiz.this, "Successfully removed question from database", Toast.LENGTH_SHORT).show();
                                        pg.setVisibility(View.GONE);
                                        remove_bt.setEnabled(true);
                                        arrayList.remove(pos);
                                        madapter.notifyItemRemoved(pos);
                                    } else {
                                        Toast.makeText(Add_quiz.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                                        pg.setVisibility(View.GONE);
                                        remove_bt.setEnabled(true);
                                    }
                                }
                            });}

                            //removing question from user root
                            if(dataSnapshot.child("users")
                                    .child(user_email)
                                    .child(title.getText().toString())
                                    .child("questions")
                                    .child(question_no).exists()) {

                                    dataSnapshot.child("users")
                                            .child(user_email)
                                            .child(title.getText().toString())
                                            .child("questions")
                                            .child(question_no)
                                            .getRef().removeValue();
                            }

                            else {
                        arrayList.remove(pos);
                        madapter.notifyItemRemoved(pos);
                    }
                } else {
                    Toast.makeText(Add_quiz.this, "Please fill in title to remove_bt the question.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});


    }

    //To add question to quiz root to firebase
    public void add_to_firebase_quiz(final String q, final String o1, final String o2,
                                     final String o3, final String o4, final int max_score, final int choice,
                                     final int pos, final ProgressBar pg, final FancyButton upload)
    {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String question_no = "question_" + (pos + 1);

                //if title is not there in quiz
                if(!dataSnapshot.child("quiz")
                        .child(title.getText().toString())
                        .child("quizes")
                        .child(user_email)
                        .exists()) {

                    pg.setVisibility(View.VISIBLE);
                    upload.setEnabled(false);

                    topic_model_firebase tmf=
                            new topic_model_firebase(title.getText().toString(),"0");

                    firebase_model fm =
                            new firebase_model(creator.getText().toString(),
                                    auth.getCurrentUser().getEmail(),
                                    title.getText().toString());

                    dataSnapshot.child("quiz")
                            .child(title.getText().toString())
                            .getRef().setValue(tmf);


                    dataSnapshot.child("quiz")
                            .child(title.getText().toString())
                            .child("quizes")
                            .child(user_email)
                            .getRef()
                            .setValue(fm);

                    question_model qm = new question_model(q,o1,o2,o3,o4,max_score,choice);


                    dataSnapshot.child("quiz")
                            .child(title.getText().toString())
                            .child("quizes")
                            .child(user_email)
                            .child("questions")
                            .child(question_no)
                            .getRef().setValue(qm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(Add_quiz.this, "added question", Toast.LENGTH_SHORT).show();
                            pg.setVisibility(View.GONE);
                            upload.setEnabled(true);
                        }
                    });
                }
                //if title is already there in quiz
                 else
                {
                    pg.setVisibility(View.VISIBLE);
                    upload.setEnabled(false);
                    question_model qm = new question_model(q,o1,o2,o3,o4,max_score,choice);

                    dataSnapshot.child("quiz")
                            .child(title.getText().toString())
                            .child("quizes")
                            .child(user_email)
                            .child("questions")
                            .child(question_no)
                            .getRef().setValue(qm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Add_quiz.this, "added question", Toast.LENGTH_SHORT).show();
                                pg.setVisibility(View.GONE);
                                upload.setEnabled(true);
                            }
                            else
                            {
                                Toast.makeText(Add_quiz.this, "error:"+task.getException(), Toast.LENGTH_SHORT).show();
                                pg.setVisibility(View.GONE);
                                upload.setEnabled(true);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //To add question to user root to firebase
    public void add_to_firebase_user(final String q, final String o1, final String o2,
                                     final String o3, final String o4, final int max_score, final int choice,
                                     final int pos) {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //if title is not there in quiz
                if(!dataSnapshot.child("users")
                        .child(user_email)
                        .child(title.getText()
                                .toString())
                        .exists()) {


                    firebase_model fm =
                            new firebase_model(creator.getText().toString(),
                                    auth.getCurrentUser().getEmail(),
                                    title.getText().toString());

                    //setting value
                    dataSnapshot.child("users")
                            .child(user_email)
                            .child(title.getText().toString())
                            .getRef().setValue(fm);

                    question_model qm =
                            new question_model(q,o1,o2,o3,o4,max_score,choice);

                    String question_no = "question_" + (pos + 1);

                    dataSnapshot.child("users")
                            .child(user_email)
                            .child(title.getText().toString())
                            .child("questions")
                            .child(question_no).getRef()
                            .setValue(qm);

                }
                //if title is already there in quiz
                 else
                {

                    question_model qm = new question_model(q,o1,o2,o3,o4,max_score,choice);
                    String question_no = "question_" + (pos + 1);
                    dataSnapshot.child("users")
                            .child(user_email)
                            .child(title.getText().toString())
                            .child("questions")
                            .child(question_no).getRef()
                            .setValue(qm);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("arr",arrayList);
    }
}
