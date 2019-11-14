package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model_classes.question_model;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class questionsAdapter extends RecyclerView.Adapter<questionsAdapter.vh> {
    private ArrayList<question_model> arr;
    private onItemClickListener listener;
    private upload upload_listener;
    static int choice=0;
    public interface onItemClickListener
    {
        void onItemClicked(int pos,ProgressBar pg, FancyButton remove);
    }
    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener=listener;
    }
    public interface upload
    {
        void uload_pressed(String q,String o1,String o2,String o3,String o4,int max_score,int radio_option,int option,ProgressBar pg, FancyButton upload);
    }
    public void setupload(questionsAdapter.upload listener)
    {
        upload_listener=listener;
    }

    public questionsAdapter(ArrayList<question_model> arrayList) {
        arr=arrayList;
    }


    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout,parent,false);
        return new vh(v,listener,upload_listener);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        question_model current=arr.get(position);
        holder.q.setText(current.getQuestion());
        holder.o1.setText(current.getOp1());
        holder.o2.setText(current.getOp2());
        holder.o3.setText(current.getOp3());
        holder.o4.setText(current.getOp4());



    }


    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class vh extends RecyclerView.ViewHolder
    {
        RadioGroup rg;
        RadioButton b1,b2,b3,b4;
        EditText q,o1,o2,o3,o4,max_score;
        FancyButton remove,upload;
        ProgressBar pg;
        public vh(@NonNull final View itemView, final onItemClickListener listener, final upload upload_listener) {
            super(itemView);
            q=itemView.findViewById(R.id.add_question_et);
            o1=itemView.findViewById(R.id.option1);
            o2=itemView.findViewById(R.id.option2);
            o3=itemView.findViewById(R.id.option3);
            o4=itemView.findViewById(R.id.option4);
            max_score=itemView.findViewById(R.id.max_score);
            b1=itemView.findViewById(R.id.r_op1);
            b2=itemView.findViewById(R.id.r_op2);
            b3=itemView.findViewById(R.id.r_op3);
            b4=itemView.findViewById(R.id.r_op4);
            rg=itemView.findViewById(R.id.radio_group);
            upload=itemView.findViewById(R.id.upload_btn);
            pg=itemView.findViewById(R.id.pg_bar);
            remove=itemView.findViewById(R.id.remove_question);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.r_op1) choice=1;
                    else if(checkedId == R.id.r_op2)choice=2;
                    else if(checkedId == R.id.r_op3)choice=3;
                    else if(checkedId == R.id.r_op4)choice=4;
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (max_score.getText().toString().isEmpty()) {
                        upload_listener.uload_pressed(q.getText().toString(), o1.getText().toString(), o2.getText().toString(), o3.getText().toString(), o4.getText().toString(), -1, choice, getAdapterPosition(), pg, upload);
                    }
                    else
                    {
                        upload_listener.uload_pressed(q.getText().toString(), o1.getText().toString(), o2.getText().toString(), o3.getText().toString(), o4.getText().toString(), Integer.parseInt(max_score.getText().toString()), choice, getAdapterPosition(), pg, upload);

                    }
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {

                            listener.onItemClicked(pos,pg,remove);
                        }
                    }
                }
            });
        }
    }
}
