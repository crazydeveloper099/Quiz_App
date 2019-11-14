package com.example.myapplication.model_classes;

public class topic_specific_quiz_model {
    String email,creator;

    public topic_specific_quiz_model(String email, String creator) {
        this.email = email;
        this.creator = creator;
    }

    public topic_specific_quiz_model() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
