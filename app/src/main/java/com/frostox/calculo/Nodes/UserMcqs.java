package com.frostox.calculo.Nodes;

/**
 * Created by shannonpereira601 on 20/04/16.
 */
public class UserMcqs {

    private String ans;
    private String date;
    private String mcqid;

    public UserMcqs() {
    }

    public UserMcqs(String ans, String date, String mcqid) {
        this.ans = ans;
        this.date = date;
        this.mcqid = mcqid;
    }


    public String getMcqid() {
        return mcqid;
    }

    public String getAns() {
        return ans;
    }

    public String getDate() {
        return date;
    }
}

