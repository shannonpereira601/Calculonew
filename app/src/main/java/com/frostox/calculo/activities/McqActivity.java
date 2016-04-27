package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.frostox.calculo.Nodes.MCQs;
import com.frostox.calculo.Nodes.Usermcq;
import com.frostox.calculo.adapters.ResultData;
import com.frostox.calculo.adapters.Resultadapter;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import calculo.frostox.com.calculo.R;

public class McqActivity extends AppCompatActivity {

    Animation animFadeIn, animFadeOut;
    TextView optionA, optionB, optionC, optionD, qn, questionnumber, click, totalques, attempted, notattempted, right, wrong, totalscore;
    ImageView imga, imgb, imgc, imgd, imgquestion;
    String id, namebar, difficulty, noq, userkey, usertopickey;
    CardView cardview;
    Button Skip;

    String[] ans, ansA, ansB, ansC, ansD, explanation, explanationType, name, question, topic, type, key;
    String[] rvqno, rvexpurl, rvurl, rvansurl, rvqn, rvans, rvexp;
    String[] usermcqkeys;
    int[] ct;
    RelativeLayout choosea, chooseb, choosec, choosed, prntrl;

    int count, scorecount;
    boolean[] checkanswer;
    int skipped, correct, page;
    boolean noqmode = false, done = false;
    RecyclerView rv;
    Resultadapter ra;
    Firebase ref, mcqref, usertopicscore, usertopicref;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        Skip = (Button) findViewById(R.id.skip);
        click = (TextView) findViewById(R.id.answer);
        questionnumber = (TextView) findViewById(R.id.questionnumber);
        imgquestion = (ImageView) findViewById(R.id.imgquestion);
        click.setVisibility(View.INVISIBLE);
        questionnumber.setVisibility(View.INVISIBLE);
        prntrl = (RelativeLayout) findViewById(R.id.prntrl);
        prntrl.setVisibility(View.INVISIBLE);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        ref = new Firebase("https://extraclass.firebaseio.com/mcqs/");

        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");
        difficulty = intent.getStringExtra("difficulty");
        noq = intent.getStringExtra("noq");
        userkey = intent.getStringExtra("userkey");
        usertopickey = intent.getStringExtra("usertopickey");

        usertopicscore = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/topics/" + usertopickey + "/score");
        mcqref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/mcqs");
        namebar = "Default";
        Query query = ref.orderByChild("topic").equalTo(id);
        count = 0;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int length = (int) dataSnapshot.getChildrenCount();
                ans = new String[length];
                ansA = new String[length];
                ansB = new String[length];
                ansC = new String[length];
                ansD = new String[length];
                type = new String[length];
                name = new String[length];
                question = new String[length];
                explanationType = new String[length];
                explanation = new String[length];
                usermcqkeys = new String[length];
                key = new String[length];
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key[count] = postSnapshot.getKey();
                    MCQs mcqtext = postSnapshot.getValue(MCQs.class);
                    if (difficulty.equals(Integer.toString(mcqtext.getDifficulty()))) {
                        ans[count] = mcqtext.getAns();
                        ansA[count] = mcqtext.getAnsA();
                        ansB[count] = mcqtext.getAnsB();
                        ansC[count] = mcqtext.getAnsC();
                        ansD[count] = mcqtext.getAnsD();
                        type[count] = mcqtext.getType();
                        name[count] = mcqtext.getName();
                        question[count] = mcqtext.getQuestion();
                        explanation[count] = mcqtext.getExplanation();
                        explanationType[count] = mcqtext.getExplanationType();
                        count++;
                    }
                }
                if (count != 0) {
                    initViews();
                } else {
                    Skip.setVisibility(View.INVISIBLE);
                    click.setVisibility(View.VISIBLE);
                    click.setText("No MCQs over here");
                    Toast.makeText(McqActivity.this, "Sorry there are no MCQs", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initViews() {

        prntrl.setVisibility(View.VISIBLE);
        ct = new int[count];
        rvqn = new String[count];
        rvqno = new String[count];
        rvurl = new String[count];
        rvansurl = new String[count];
        rvexpurl = new String[count];
        rvans = new String[count];
        rvexp = new String[count];

        cardview = (CardView) findViewById(R.id.card_view);
        choosea = (RelativeLayout) findViewById(R.id.choosea);
        chooseb = (RelativeLayout) findViewById(R.id.chooseb);
        choosec = (RelativeLayout) findViewById(R.id.choosec);
        choosed = (RelativeLayout) findViewById(R.id.choosed);

        qn = (TextView) findViewById(R.id.question);
        optionA = (TextView) findViewById(R.id.ansa);
        optionB = (TextView) findViewById(R.id.ansb);
        optionC = (TextView) findViewById(R.id.ansc);
        optionD = (TextView) findViewById(R.id.ansd);
        totalques = (TextView) findViewById(R.id.totques);
        totalscore = (TextView) findViewById(R.id.totalscore);
        right = (TextView) findViewById(R.id.right);
        wrong = (TextView) findViewById(R.id.wrong);
        attempted = (TextView) findViewById(R.id.attempted);
        notattempted = (TextView) findViewById(R.id.notattempted);
        scorecount = 0;
        click = (TextView) findViewById(R.id.answer);
        questionnumber = (TextView) findViewById(R.id.questionnumber);
        imga = (ImageView) findViewById(R.id.imga);
        imgb = (ImageView) findViewById(R.id.imgb);
        imgc = (ImageView) findViewById(R.id.imgc);
        imgd = (ImageView) findViewById(R.id.imgd);
        imgquestion = (ImageView) findViewById(R.id.imgquestion);
        click.setVisibility(View.VISIBLE);
        questionnumber.setVisibility(View.VISIBLE);
        qn.setVisibility(View.INVISIBLE);
        imgquestion.setVisibility(View.INVISIBLE);
        textinvisible();
        imginvisible();
        TouchListener(choosea, "A");
        TouchListener(chooseb, "B");
        TouchListener(choosec, "C");
        TouchListener(choosed, "D");
        TouchListener(Skip, "skip");
        page = 0;
        Log.d("Testreachedinit", type[page] + ".." + ansA[page]);
        checkanswer = new boolean[count];
        load(0);
    }

    public void onClickPrev(View v) {
        --page;
        if (page >= (-1)) {
            load(page);
        } else {
            Toast.makeText(McqActivity.this, "No MCQs Behind", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickNext(View v) {
        ++page;

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

      /* if (v == null && !skip)
            Snackbar.make(viewGroup, "Correct!", Snackbar.LENGTH_LONG).show();
        else if(v!=null && !skip)Snackbar.make(viewGroup, "Wrong!", Snackbar.LENGTH_LONG).show();
        else Snackbar.make(viewGroup, "Skipped!", Snackbar.LENGTH_LONG).show();*/

        if (page == (Integer.parseInt(noq))) {
            done = true;
            noqmode = true;
            Toast.makeText(McqActivity.this, "All Done!", Toast.LENGTH_LONG).show();
            getSupportActionBar().setTitle("Result");
            textinvisible();
            imginvisible();
            prntrl.setVisibility(View.INVISIBLE);
            cardview.setVisibility(View.VISIBLE);
            count = Integer.parseInt(noq);

            usertopicscore.setValue(scorecount);
            totalques.setText("Total Questions: " + count);
            attempted.setText("Attempted: " + (count - skipped));
            notattempted.setText("Not Attempted: " + skipped);
            right.setText("Right Answer: " + scorecount);
            wrong.setText("Wrong Answer: " + (count - scorecount - skipped));
            totalscore.setText("Total Score: " + scorecount);
            rv.setVisibility(View.VISIBLE);
            ra = new Resultadapter(this, getdata());
            rv.setAdapter(ra);
        }

        if (page != count && count != 0) {
            load(page);
        } else if (page >= count) {
            done = true;
            Snackbar.make(viewGroup, "All Done!", Snackbar.LENGTH_LONG).show();
            Skip.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Result");
            textinvisible();
            imginvisible();

            usertopicscore.setValue(scorecount);
            prntrl.setVisibility(View.INVISIBLE);
            cardview.setVisibility(View.VISIBLE);
            if (!noqmode)
                totalques.setText("Total Questions: " + count);
            totalques.setText("Total Questions: " + count);
            attempted.setText("Attempted: " + (count - skipped));
            notattempted.setText("Not Attempted: " + skipped);
            right.setText("Right Answer: " + scorecount);
            wrong.setText("Wrong Answer: " + (count - scorecount - skipped));
            totalscore.setText("Total Score: " + scorecount);
            rv.setVisibility(View.VISIBLE);
            ra = new Resultadapter(this, getdata());
            rv.setAdapter(ra);
        }

    }

    public void load(int i) {
        if (type[i].equals("text")) {
            textvisible();
            imginvisible();
            qn.setText(question[i]);
            optionA.setText("(a) " + ansA[i]);
            optionB.setText("(b) " + ansB[i]);
            optionC.setText("(c) " + ansC[i]);
            optionD.setText("(d) " + ansD[i]);
            questionnumber.setText("Q." + (i + 1));
            rvqno[i] = ("Q." + (i + 1));
            rvqn[i] = question[i];
            rvurl[i] = "http://www.frostox.com/extraclass/uploads/";

            if (ans[i].equals("A")) {
                rvans[i] = ansA[i];
            } else if (ans[i].equals("B")) {
                rvans[i] = ansB[i];
            } else if (ans[i].equals("C")) {
                rvans[i] = ansC[i];
            } else if (ans[i].equals("D")) {
                rvans[i] = ansD[i];
            }
            rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/");


        } else if (type[i].equals("image")) {
            textinvisible();
            imgvisible();
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "quest").networkPolicy(NetworkPolicy.OFFLINE).into(imgquestion);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "A").networkPolicy(NetworkPolicy.OFFLINE).into(imga);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "B").networkPolicy(NetworkPolicy.OFFLINE).into(imgb);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "C").networkPolicy(NetworkPolicy.OFFLINE).into(imgc);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "D").networkPolicy(NetworkPolicy.OFFLINE).into(imgd);
            questionnumber.setText("Q." + (i + 1));
            rvqno[i] = ("Q." + (i + 1));
            rvqn[i] = "";
            rvurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "quest");

            if (ans[i].equals("A")) {
                rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "A");
            } else if (ans[i].equals("B")) {
                rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "B");
            } else if (ans[i].equals("C")) {
                rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "C");
            } else if (ans[i].equals("D")) {
                rvansurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "D");
            }
            rvans[i] = "";
        }
        if (explanationType[i].equals("text")) {
            rvexp[i] = explanation[i];
            rvexpurl[i] = "http://www.frostox.com/extraclass/uploads/";
        } else if (explanationType[i].equals("image")) {
            rvexpurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "explanation");
            rvexp[i] = "";
        }

        getSupportActionBar().setTitle(name[i]);
    }

   /* public void vibrateDevice() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }*/

    public void TouchListener(View v, final String option) {

        final String check = String.valueOf(v.getId());
        v.setOnTouchListener(new View.OnTouchListener() {
            public final static int FINGER_RELEASED = 0;
            public final static int FINGER_TOUCHED = 1;
            public final static int FINGER_DRAGGING = 2;
            public final static int FINGER_UNDEFINED = 3;
            private int fingerState = FINGER_RELEASED;
            private String state;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                        else fingerState = FINGER_UNDEFINED;

                        if (ans != null) {

                            Usermcq mcqs = null;


                            if (ans[page].equals(option)) {
                                ct[page] = R.drawable.mark;
                                state = "Correct";
                                checkanswer[page] = true;
                                scorecount++;
                                onClickNext(null);

                            } else if (option.equals("skip")) {
                                ct[page] = R.drawable.skip;
                                state = "Skip";
                                skipped++;
                                checkanswer[page] = false;
                                onClickNext(null);
                            } else {
                                ct[page] = R.drawable.cross;
                                state = "Wrong";
                                checkanswer[page] = false;
                                onClickNext((View) findViewById(R.id.dummy));
                            }
                            Log.d("Testreachedtouch", state + ".." + usertopickey + ".." + type[page - 1] + ".." + ansA[page - 1]);
                            if (ans[page - 1].equals("A"))
                                mcqs = new Usermcq(usertopickey, key[page - 1], state + " " + ans[page - 1], ansA[page - 1], question[page - 1], type[page - 1]);
                            else if (ans[page - 1].equals("B"))
                                mcqs = new Usermcq(usertopickey, key[page - 1], state + " " + ans[page - 1], ansA[page - 1], question[page - 1], type[page - 1]);
                            else if (ans[page - 1].equals("C"))
                                mcqs = new Usermcq(usertopickey, key[page - 1], state + " " + ans[page - 1], ansA[page - 1], question[page - 1], type[page - 1]);
                            else if (ans[page - 1].equals("D"))
                                mcqs = new Usermcq(usertopickey, key[page - 1], state + " " + ans[page - 1], ansA[page - 1], question[page - 1], type[page - 1]);
                            Firebase push = mcqref.push();
                            usermcqkeys[page - 1] = push.getKey();
                            push.setValue(mcqs);
                            animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.mcqs);
                            animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.mcqs);
                            prntrl.startAnimation(animFadeIn);
                            //animin.start();
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        if (fingerState != FINGER_DRAGGING) {
                            fingerState = FINGER_RELEASED;

                        } else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING)
                            fingerState = FINGER_DRAGGING;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    default:
                        fingerState = FINGER_UNDEFINED;

                }

                return false;
            }
        });

    }

    public List<ResultData> getdata() {
        List<ResultData> input = new ArrayList<>();
        int length;
        if (noqmode) length = (Integer.parseInt(noq));
        else length = count;
        for (int i = 0; i < length; i++) {
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

    public void textvisible() {
        qn.setVisibility(View.VISIBLE);
        optionA.setVisibility(View.VISIBLE);
        optionB.setVisibility(View.VISIBLE);
        optionC.setVisibility(View.VISIBLE);
        optionD.setVisibility(View.VISIBLE);
    }

    public void textinvisible() {
        qn.setVisibility(View.INVISIBLE);
        optionA.setVisibility(View.INVISIBLE);
        optionB.setVisibility(View.INVISIBLE);
        optionC.setVisibility(View.INVISIBLE);
        optionD.setVisibility(View.INVISIBLE);
    }

    public void imgvisible() {
        imgquestion.setVisibility(View.VISIBLE);
        imga.setVisibility(View.VISIBLE);
        imgb.setVisibility(View.VISIBLE);
        imgc.setVisibility(View.VISIBLE);
        imgd.setVisibility(View.VISIBLE);
    }

    public void imginvisible() {
        imgquestion.setVisibility(View.INVISIBLE);
        imga.setVisibility(View.INVISIBLE);
        imgb.setVisibility(View.INVISIBLE);
        imgc.setVisibility(View.INVISIBLE);
        imgd.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (!done) {
            usertopicref = new Firebase("https://extraclass.firebaseio.com/users/" + userkey + "/topics/" + usertopickey);
            usertopicref.removeValue();
            for (int i = 0; i < usermcqkeys.length; i++) {
                if (usermcqkeys[i] != null)
                    mcqref.child(usermcqkeys[i]).removeValue();
            }
        }
        super.onBackPressed();

    }
}

