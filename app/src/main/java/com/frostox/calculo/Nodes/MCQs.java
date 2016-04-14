package com.frostox.calculo.Nodes;

/**
 * Created by admin on 15/04/2016.
 */
public class MCQs {

    private String ans;
    private String ansA;
    private String ansB;
    private String ansC;
    private String ansD;
    private int difficulty;
    private String question;
    private String topic;
    private String type;

    public MCQs() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String getAns() {
        return ans;
    }
    public String getAnsA() {
        return ansA;
    }
    public String getAnsB() {
        return ansB;
    }
    public String getAnsC() {
        return ansC;
    }
    public String getAnsD() {
        return ansD;
    }
    public String getQuestion() {
        return question;
    }
    public String getTopic() {
        return topic;
    }
    public String getType() {
        return type;
    }
    public int getDifficulty() {
        return difficulty;
    }

}
