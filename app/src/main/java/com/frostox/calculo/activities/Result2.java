package com.frostox.calculo.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.frostox.calculo.Nodes.MCQs;
import com.frostox.calculo.Nodes.Standards;
import com.frostox.calculo.Nodes.Usermcq;
import com.frostox.calculo.adapters.Data;
import com.frostox.calculo.adapters.RecyclerViewAdapter;
import com.frostox.calculo.adapters.ResultData;
import com.frostox.calculo.adapters.Resultadapter;
import com.frostox.calculo.fragments.EntityFragment1;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import calculo.frostox.com.calculo.R;

public class Result2 extends AppCompatActivity {

    private RecyclerView recyclerView, rv;
    Resultadapter ra;
    int mcqcount = 0;
    Firebase ref, userref, mcqref;

    String[] rvqno, rvexpurl, rvurl, rvansurl, rvqn, rvans, rvexp;

    String ans, ansA, ansB, ansC, ansD, explanation, explanationType, name, question, topic, type;

    int[] ct;
    int i = 0;
    List<ResultData> input = new ArrayList<>();
    private String[] key;
    String mcqkeys;
    String userid;
    private FirebaseRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ref = new Firebase("https://extraclass.firebaseio.com/");
        AuthData authData = ref.getAuth();
        userid = authData.getUid();
        getUserKey();

    /*    final Firebase listref = ref.child("topics");
        Query query = listref.orderByChild("subject");
        getKey(query);
        recyclerAdapter = new FirebaseRecyclerAdapter<Standards, EntityFragment1.DataObjectHolder>(Standards.class, R.layout.recycler_view_item, EntityFragment1.DataObjectHolder.class, query) {
            @Override
            public void populateViewHolder(EntityFragment1.DataObjectHolder dataObjectHolder, final Standards standards, final int position) {
                dataObjectHolder.label.setText(standards.getName());
                dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = standards.getName();

                    }
                });
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recyclerAdapter);*/


        //  RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(entities);

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

    public void getUserKey() {

        userref = ref.child("users");
        Query query = userref.orderByChild("uid").equalTo(userid);
        final String[] keyid = new String[1];
        query.addValueEventListener(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int length = (int) dataSnapshot.getChildrenCount();
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                keyid[0] = postSnapshot.getKey();
                                            }
                                            Log.d("Resultukey", keyid[0]);
                                            mcqref = new Firebase("https://extraclass.firebaseio.com/users/" + keyid[0] + "/mcqs/");

                                            mcqref.addValueEventListener(new ValueEventListener() {
                                                                             @Override
                                                                             public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                 for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                                     Usermcq usermcq = postSnapshot.getValue(Usermcq.class);
                                                                                     Log.d("keysare", usermcq.getMcqid());
                                                                                     mcqkeys = usermcq.getMcqid();
                                                                                     fetchsinglemcqdata(mcqkeys);
                                                                                 }

                                                                                 rv = (RecyclerView) findViewById(R.id.rv);
                                                                                 LinearLayoutManager llm = new LinearLayoutManager(Result2.this);
                                                                                 llm.setOrientation(LinearLayoutManager.VERTICAL);
                                                                                 rv.setLayoutManager(llm);
                                                                                 ra=new Resultadapter(Result2.this, input);
                                                                                 rv.setAdapter(ra);
                                                                             }

                                                                             @Override
                                                                             public void onCancelled(FirebaseError firebaseError) {

                                                                             }
                                                                         }

                                            );

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {
                                            System.out.println("The read failed: " + firebaseError.getMessage());
                                        }
                                    }

        );
    }

    public List<ResultData> getdata() {
        List<ResultData> input = new ArrayList<>();
        for (int i = 0; i < rvans.length; i++) {
            ResultData current = new ResultData(rvurl[i], rvansurl[i], rvexpurl[i], rvqno[i], rvqn[i], rvans[i], rvexp[i], ct[i]);
            current.ct = ct[i];
            current.imgansurl = rvansurl[i];
            current.imgqnurl = rvurl[i];
            current.expurl = rvexpurl[i];
            current.qno = (rvqno[i]);
            current.qn = rvqn[i];
            current.ans = rvans[i];
            current.exp = rvexp[i];
            input.add(current);
        }
        return input;
    }


    public void fetchsinglemcqdata(String id) {
        Firebase ref1 = ref.child("mcqs");
        Query query = ref1.equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MCQs mcqtext = postSnapshot.getValue(MCQs.class);

                    ans = mcqtext.getAns();
                    ansA = mcqtext.getAnsA();
                    ansB = mcqtext.getAnsB();
                    ansC = mcqtext.getAnsC();
                    ansD = mcqtext.getAnsD();
                    type = mcqtext.getType();
                    name = mcqtext.getName();
                    question = mcqtext.getQuestion();
                    explanation = mcqtext.getExplanation();
                    explanationType = mcqtext.getExplanationType();


                    if (type.equals("text")) {
                        rvqno[i] = ("Q." + (i + 1));
                        rvqn[i] = question;
                        rvurl[i] = "http://www.frostox.com/extraclass/uploads/";
                        if (ans.equals("A")) {
                            rvans[i] = ansA;
                        } else if (ans.equals("B")) {
                            rvans[i] = ansB;
                        } else if (ans.equals("C")) {
                            rvans[i] = ansC;
                        } else if (ans.equals("D")) {
                            rvans[i] = ansD;
                        }
                        rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/");

                    } else if (type.equals("image")) {
                        rvqno[i] = ("Q." + (i + 1));
                        rvqn[i] = "";
                        rvurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "quest");

                        if (ans.equals("A")) {
                            rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "A");
                        } else if (ans.equals("B")) {
                            rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "B");
                        } else if (ans.equals("C")) {
                            rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "C");
                        } else if (ans.equals("D")) {
                            rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "D");
                        }
                        rvans[i] = "";
                    }
                    if (explanationType.equals("text")) {
                        rvexp[i] = explanation;
                        rvexpurl[i] = "http://www.frostox.com/extraclass/uploads/";
                    } else if (explanationType.equals("image")) {
                        rvexpurl[i] = ("http://www.frostox.com/extraclass/uploads/" + mcqkeys + "explanation");
                        rvexp[i] = "";
                    }

                    ResultData current = new ResultData(rvurl[i], rvansurl[i], rvexpurl[i], rvqno[i], rvqn[i], rvans[i], rvexp[i], ct[i]);
                    current.ct = ct[i];
                    current.imgansurl = rvansurl[i];
                    current.imgqnurl = rvurl[i];
                    current.expurl = rvexpurl[i];
                    current.qno = (rvqno[i]);
                    current.qn = rvqn[i];
                    current.ans = rvans[i];
                    current.exp = rvexp[i];
                    input.add(current);
                    ++i;
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }
    /*public List<Data> getdata() {
        List<Data> input = new ArrayList<>();
        int length;
        for (int i = 0; i < length; i++) {
            Data current = new Data(rvurl[i]);
            current.text = ct[i];
            input.add(current);
        }
        return input;
    }*/
}