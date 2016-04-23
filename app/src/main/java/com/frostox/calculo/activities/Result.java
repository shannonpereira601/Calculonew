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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.frostox.calculo.Nodes.Standards;
import com.frostox.calculo.Nodes.Usermcq;
import com.frostox.calculo.Nodes.Usertopics;
import com.frostox.calculo.fragments.EntityFragment1;
import com.squareup.picasso.Picasso;

import calculo.frostox.com.calculo.R;

public class Result extends AppCompatActivity {
    private FirebaseRecyclerAdapter recyclerAdapter, recyclerAdapter2;
    private Firebase usertopicref, usermcqref;
    private String userkey;
    private String[] key;
    private RecyclerView recyclerView, recyclerView2;
    private boolean topicmode = true;
    private int counter = 1;

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

        usertopicref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/topics");
        getKey(usertopicref);
        usermcqref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/mcqs");
        Query query = usertopicref.orderByChild("name");

        recyclerAdapter = new FirebaseRecyclerAdapter<Usertopics, EntityFragment1.DataObjectHolder>(Usertopics.class, R.layout.recycler_view_item, EntityFragment1.DataObjectHolder.class, query) {
            @Override
            public void populateViewHolder(EntityFragment1.DataObjectHolder dataObjectHolder, final Usertopics usertopics, final int position) {
                dataObjectHolder.label.setText(usertopics.getName());
                dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        counter = 1;
                        topicmode = false;
                        Query query1 = usermcqref.orderByChild("topic").equalTo(key[position]);
                        recyclerAdapter2 = new FirebaseRecyclerAdapter<Usermcq, MyViewHolder>(Usermcq.class, R.layout.navresultitem, MyViewHolder.class, query1) {
                            @Override
                            public void populateViewHolder(MyViewHolder myViewHolder, final Usermcq usermcq, final int position2) {
                                myViewHolder.questionnumber.setText("Q." + counter);
                                myViewHolder.question.setText(usermcq.getQuestion());
                                myViewHolder.answer.setText(usermcq.getAnswer());
                                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "question").into(myViewHolder.imgquestion);
                                Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "A").into(myViewHolder.imganswer);
                                if (usermcq.getState().contains("Correct"))
                                    myViewHolder.ct.setImageResource(R.drawable.mark);
                                else if (usermcq.getState().contains("Wrong"))
                                    myViewHolder.ct.setImageResource(R.drawable.cross);
                                counter++;
                            }
                        };

                        recyclerView2.setAdapter(recyclerAdapter2);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
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

        recyclerView.setAdapter(recyclerAdapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgquestion;
        public ImageView imganswer;
        public ImageView ct;
        public TextView questionnumber;
        public TextView answer;
        public TextView question;

        public MyViewHolder(View v) {
            super(v);
            ct = (ImageView) v.findViewById(R.id.ct);
            imganswer = (ImageView) v.findViewById(R.id.imganswer);
            imgquestion = (ImageView) v.findViewById(R.id.imgquestion);
            questionnumber = (TextView) v.findViewById(R.id.questionnumber);
            question = (TextView) v.findViewById(R.id.question);
            answer = (TextView) v.findViewById(R.id.answer);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!topicmode) {
            recyclerView2.setVisibility(View.GONE);
            recyclerView.setAdapter(recyclerAdapter);
            topicmode = true;
        } else {
            finish();
            return;
        }
    }


    public void getKey(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            int count = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int length = (int) dataSnapshot.getChildrenCount();
                key = new String[length];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key[count] = postSnapshot.getKey();
                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}