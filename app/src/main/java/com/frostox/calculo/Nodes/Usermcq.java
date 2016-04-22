package com.frostox.calculo.Nodes;

/**
 * Created by shannonpereira601 on 20/04/16.
 */
public class Usermcq{

    private String mcqid;

    public String getTopic() {
        return topic;
    }

    private String topic;
    private String state;

    public Usermcq() {
    }

    public Usermcq(String topic, String mcqid, String state) {
        this.mcqid = mcqid;
        this.topic = topic;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getMcqid() {
        return mcqid;
    }

}

