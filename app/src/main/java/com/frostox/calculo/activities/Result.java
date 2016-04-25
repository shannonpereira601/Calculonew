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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.frostox.calculo.Nodes.Usermcq;
import com.frostox.calculo.Nodes.Usertopics;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import calculo.frostox.com.calculo.R;

public class Result extends AppCompatActivity {
    private FirebaseRecyclerAdapter recyclerAdapter, recyclerAdapter2;
    private Firebase usertopicref, usermcqref;
    private String userkey;
    private String[] topickey, mcqtopickey;
    private RecyclerView recyclerView, recyclerView2;
    private boolean topicmode = true;
    int counter, count1, count2;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView2 = (RecyclerView) findViewById(R.id.rv2);
        clear = (Button)findViewById(R.id.clear);
        recyclerView.setVisibility(View.VISIBLE);

        final LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        Intent intent = this.getIntent();
        userkey = intent.getStringExtra("userkey");

        usertopicref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/topics");
        usermcqref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/mcqs");
        getKey(usertopicref);
        checkmcqs();
        //

        recyclerAdapter = new FirebaseRecyclerAdapter<Usertopics, DataObjectHolder>(Usertopics.class, R.layout.resulttopicitem, DataObjectHolder.class, usertopicref) {
            @Override
            public void populateViewHolder(DataObjectHolder dataObjectHolder, final Usertopics usertopics, final int position) {

                dataObjectHolder.label.setText(usertopics.getName());
                dataObjectHolder.difficulty.setText("Difficulty: " + usertopics.getDifficulty());
                dataObjectHolder.date.setText("Date: " + usertopics.getTimestamp());
                dataObjectHolder.ll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        recyclerView2.setVisibility(View.VISIBLE);
                        counter = 1;
                        topicmode = false;
                        Query query1 = usermcqref.orderByChild("topic").equalTo(topickey[position]);
                        recyclerAdapter2 = new FirebaseRecyclerAdapter<Usermcq, MyViewHolder>(Usermcq.class, R.layout.navresultitem, MyViewHolder.class, query1) {
                            @Override
                            public void populateViewHolder(MyViewHolder myViewHolder, final Usermcq usermcq, final int position2) {
                                myViewHolder.questionnumber.setText("Q." + (position2+1));
                                if (usermcq.getType().equals("text")) {
                                    myViewHolder.question.setText(usermcq.getQuestion());
                                    myViewHolder.answer.setText(usermcq.getAnswer());
                                   // Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/");
                                   // Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/");
                                    myViewHolder.imgquestion.setVisibility(View.GONE);
                                    myViewHolder.imganswer.setVisibility(View.GONE);

                                } else if (usermcq.getType().equals("image")) {
                                    myViewHolder.question.setText("");
                                    myViewHolder.answer.setText("");
                                    myViewHolder.imgquestion.setVisibility(View.VISIBLE);
                                    myViewHolder.imganswer.setVisibility(View.VISIBLE);
                                    Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + "quest").into(myViewHolder.imgquestion);
                                    String answer = usermcq.getState().substring(usermcq.getState().length() - 1);
                                    Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + usermcq.getMcqid() + answer).into(myViewHolder.imganswer);
                                }
                                if (usermcq.getState().contains("Correct"))
                                    myViewHolder.ct.setImageResource(R.drawable.mark);
                                else if (usermcq.getState().contains("Wrong"))
                                    myViewHolder.ct.setImageResource(R.drawable.cross);
                                else if (usermcq.getState().contains("Skip"))
                                    myViewHolder.ct.setImageResource(R.drawable.skip);
                            }
                        };
                        notifyItemInserted(position);

                        recyclerView2.setLayoutManager(llm2);
                        recyclerView2.setAdapter(recyclerAdapter2);
                        clear.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });


            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recyclerAdapter);

    }


    private static class DataObjectHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView difficulty;
        public TextView date;
        public LinearLayout ll;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.recycler_view_item_text);
            difficulty = (TextView) itemView.findViewById(R.id.difficulty);
            date = (TextView) itemView.findViewById(R.id.date);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
        }

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
        //  super.onBackPressed();
        if (!topicmode) {
            Log.d("checking", "" + topicmode);
            recyclerView2.setVisibility(View.GONE);
            recyclerView.setAdapter(recyclerAdapter);
            clear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            topicmode = true;
        } else {
            finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
        if (!topicmode) {
            recyclerAdapter2.cleanup();
        }
    }

    public void Clear(View v)
    {
      //  recyclerView.setVisibility(View.GONE);
        usermcqref.removeValue();
    }

    public void getKey(Query query) {
        query.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 = 0;
                int length = (int) dataSnapshot.getChildrenCount();
                topickey = new String[length];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    topickey[count1] = postSnapshot.getKey();
                    Usertopics usertopics = postSnapshot.getValue(Usertopics.class);
                    Log.d("Checkingtopic", topickey[count1]);
                    count1++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void checkmcqs() {
        usermcqref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count2 = 0;
                int length = (int) dataSnapshot.getChildrenCount();
                mcqtopickey = new String[length];
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usermcq usermcq = postSnapshot.getValue(Usermcq.class);
                    mcqtopickey[count2] = usermcq.getTopic();
                    Log.d("Checkingmcqtopic", mcqtopickey[count2]);
                    count2++;
                }
                Set<String> VALUES = new HashSet<String>(Arrays.asList(mcqtopickey));
                for (int i = 0; i < count1; i++) {
                    if (!VALUES.contains(topickey[i])) {
                        usertopicref.child(topickey[i]).removeValue();
                        // Log.d("Checkingloop",""+i);
                    }
                }
                getKey(usertopicref);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}