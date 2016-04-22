package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.frostox.calculo.Nodes.Standards;
import com.frostox.calculo.Nodes.Usermcq;
import com.frostox.calculo.Nodes.Usertopics;
import com.frostox.calculo.fragments.EntityFragment1;
import com.squareup.picasso.Picasso;

import calculo.frostox.com.calculo.R;

public class Result extends AppCompatActivity {
    private FirebaseRecyclerAdapter recyclerAdapter,recyclerAdapter2;
    private Firebase topicref, mcqref;
    private String userkey;
    private RecyclerView recyclerView, recyclerView2;
    private boolean topicmode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView2 = (RecyclerView) findViewById(R.id.rv2);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = this.getIntent();
        userkey = intent.getStringExtra("userkey");

        topicref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/topics");
        mcqref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/mcqs");
        Query query = topicref.orderByChild("name");

        recyclerAdapter = new FirebaseRecyclerAdapter<Usertopics, EntityFragment1.DataObjectHolder>(Usertopics.class, R.layout.recycler_view_item, EntityFragment1.DataObjectHolder.class, query) {
            @Override
            public void populateViewHolder(EntityFragment1.DataObjectHolder dataObjectHolder, final Usertopics usertopics, final int position) {
                dataObjectHolder.label.setText(usertopics.getName());
            }
        };

       /* recyclerAdapter2 = new FirebaseRecyclerAdapter<Usermcq, MyViewHolder>(Usermcq.class, R.layout.resultitem, MyViewHolder.class, mcqref) {
            @Override
            public void populateViewHolder(MyViewHolder myViewHolder, final Usermcq usermcq, final int position) {
                myViewHolder.questionnumber.setText(usermcq.getMcqid());
                myViewHolder.question.setText(usermcq.getMcqid());
                myViewHolder.answer.setText(usermcq.getMcqid());
                myViewHolder.explanation.setText(usermcq.getState());
                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "question").into(myViewHolder.ct);
                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "question").into(myViewHolder.imgquestion);
                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "A").into(myViewHolder.imganswer);
                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "explanation").into(myViewHolder.imgexplanation);
            }
        };*/

        recyclerAdapter2 = new FirebaseRecyclerAdapter<Usermcq, MyViewHolder>(Usermcq.class, R.layout.recycler_view_item, MyViewHolder.class, mcqref) {
            @Override
            public void populateViewHolder(MyViewHolder myViewHolder, final Usermcq usermcq, final int position) {
                myViewHolder.question.setText(usermcq.getMcqid());
            }
        };

        recyclerView2.setAdapter(recyclerAdapter2);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgquestion;
        public ImageView imgexplanation;
        public ImageView imganswer;
        public ImageView ct;
        public TextView questionnumber;
        public TextView question;
        public TextView answer;
        public TextView explanation;
        public MyViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.recycler_view_item_text);
        }

    }
}