package com.example.myapplication.model_classes;

import android.os.Parcel;
import android.os.Parcelable;

public class question_model implements Parcelable {
    String question,op1,op2,op3,op4;
    public int choice;
    public int max_score;
    public question_model() {
    }



    public question_model(Parcel in) {
        question = in.readString();
        op1 = in.readString();
        op2 = in.readString();
        op3 = in.readString();
        op4 = in.readString();
    }

    public static final Creator<question_model> CREATOR = new Creator<question_model>() {
        @Override
        public question_model createFromParcel(Parcel in) {
            return new question_model(in);
        }

        @Override
        public question_model[] newArray(int size) {
            return new question_model[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public question_model(String question, String op1, String op2, String op3, String op4,int max_score,int choice) {
        this.question = question;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.max_score=max_score;
        this.choice=choice;
    }

    public int getMax_score() {
        return max_score;
    }

    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(op1);
        parcel.writeString(op2);
        parcel.writeString(op3);
        parcel.writeString(op4);
    }
}
