package com.example.myapplication.model_classes;

public class topic_model {
    private String players,topic;

    public topic_model()
    {

    }



    public topic_model( String players,String topic) {
        this.topic = topic;

        this.players=players;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }



    public String getNumber_of_players() {
        return players;
    }

    public void setNumber_of_players(String players) {
        this.players = players;
    }
}
