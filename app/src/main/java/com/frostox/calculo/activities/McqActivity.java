package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.frostox.calculo.Entities.McqItem;
import com.frostox.calculo.Nodes.MCQs;
import com.frostox.calculo.adapters.ResultData;
import com.frostox.calculo.adapters.Resultadapter;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.dao.McqItemDao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import calculo.frostox.com.calculo.R;
import io.github.kexanie.library.MathView;

public class McqActivity extends AppCompatActivity {


    TextView optionA, optionB, optionC, optionD, qn, questionnumber, click, score;
    ImageView imga, imgb, imgc, imgd, imgquestion;
    String id, namebar, difficulty;

    String[] ans, ansA, ansB, ansC, ansD, explanation, explanationType, name, question, topic, type, key;
    String[] rvqno, rvurl, rvqn, rvans, rvexp;
    String[] answercheck;
    LinearLayout.LayoutParams fill, empty;

    LinearLayout lltext, llimg;

    int count, scorecount;
    int page;

    RecyclerView rv;
    Resultadapter ra;
    Firebase ref;

    private List<McqItem> mcqItems;

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
        fill = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        empty = new LinearLayout.LayoutParams(0, 0);
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        ref = new Firebase("https://extraclass.firebaseio.com/mcqs/");
        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");
        difficulty = intent.getStringExtra("difficulty");
        namebar = "Default";
        Query query = ref.orderByChild("topic").equalTo(id);
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
                explanation = new String[length];
                key = new String[length];
                count = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key[count] = postSnapshot.getKey();
                    MCQs mcqtext = postSnapshot.getValue(MCQs.class);
                    //  if (difficulty.equals(Integer.toString(mcqtext.getDifficulty()))) {
                    ans[count] = mcqtext.getAns();
                    ansA[count] = mcqtext.getAnsA();
                    ansB[count] = mcqtext.getAnsB();
                    ansC[count] = mcqtext.getAnsC();
                    ansD[count] = mcqtext.getAnsD();
                    type[count] = mcqtext.getType();
                    name[count] = mcqtext.getName();
                    question[count] = mcqtext.getQuestion();
                    explanation[count] = mcqtext.getExplanation();
                    count++;
                    //  }
                }
                if (count != 0) {
                    initViews();
                } else {
                    lltext.setVisibility(View.INVISIBLE);
                    llimg.setVisibility(View.INVISIBLE);
                    questionnumber.setVisibility(View.INVISIBLE);
                    qn.setVisibility(View.INVISIBLE);
                    imgquestion.setVisibility(View.INVISIBLE);
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

        rvqn = new String[count];
        rvqno = new String[count];
        rvurl = new String[count];
        rvans = new String[count];
        rvexp = new String[count];

        lltext = (LinearLayout) findViewById(R.id.optiontext);
        llimg = (LinearLayout) findViewById(R.id.optionimg);

        answercheck = new String[count];
        answercheck[0] = "incorrect";
        qn = (TextView) findViewById(R.id.question);
        optionA = (TextView) findViewById(R.id.ansa);
        optionB = (TextView) findViewById(R.id.ansb);
        optionC = (TextView) findViewById(R.id.ansc);
        optionD = (TextView) findViewById(R.id.ansd);
        score = (TextView) findViewById(R.id.score);
        scorecount = 0;
        click = (TextView) findViewById(R.id.answer);
        questionnumber = (TextView) findViewById(R.id.questionnumber);
        imga = (ImageView) findViewById(R.id.imga);
        imgb = (ImageView) findViewById(R.id.imgb);
        imgc = (ImageView) findViewById(R.id.imgc);
        imgd = (ImageView) findViewById(R.id.imgd);
        imgquestion = (ImageView) findViewById(R.id.imgquestion);
        lltext.setVisibility(View.INVISIBLE);
        llimg.setVisibility(View.INVISIBLE);
        questionnumber.setVisibility(View.INVISIBLE);
        qn.setVisibility(View.INVISIBLE);
        imgquestion.setVisibility(View.INVISIBLE);
        TouchListener(imga, "A");
        TouchListener(imgb, "B");
        TouchListener(imgc, "C");
        TouchListener(imgd, "D");
        TouchListener(optionA, "A");
        TouchListener(optionB, "B");
        TouchListener(optionC, "C");
        TouchListener(optionD, "D");
        page = 0;
        load(0);

    }


    public void onClickPrev(View v) {
        --page;
        if (page >= (-1)) {
            load(page);
        } else {
            Toast.makeText(McqActivity.this, "No MCQs Behind", Toast.LENGTH_LONG).show();
            lltext.setVisibility(View.INVISIBLE);
            llimg.setVisibility(View.INVISIBLE);
        }

    }

    public void onClickNext(View v) {
        ++page;
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        if (v == null)
            Snackbar.make(viewGroup, "Correct!", Snackbar.LENGTH_LONG).show();

        if (v != null)
            Snackbar.make(viewGroup, "Wrong!", Snackbar.LENGTH_LONG).show();

        if (page != count && count != 0) {
            load(page);

        } else if (page >= count) {
            Toast.makeText(McqActivity.this, "No More MCQs", Toast.LENGTH_LONG).show();
            getSupportActionBar().setTitle("Result");
            lltext.setVisibility(View.INVISIBLE);
            llimg.setVisibility(View.INVISIBLE);
            score.setVisibility(View.VISIBLE);
            score.setText("Your score is: " + scorecount);
            questionnumber.setVisibility(View.INVISIBLE);
            imgquestion.setVisibility(View.INVISIBLE);
            qn.setVisibility(View.INVISIBLE);
            click.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
            ra = new Resultadapter(this, getdata());
            rv.setAdapter(ra);
        }
    }

    public void load(int i) {
        questionnumber.setVisibility(View.VISIBLE);

        if (type[i].equals("text")) {
            llimg.setVisibility(View.INVISIBLE);
            lltext.setVisibility(View.VISIBLE);
            //llimg.setLayoutParams(empty);
            //lltext.setLayoutParams(fill);
            qn.setVisibility(View.VISIBLE);
            imgquestion.setVisibility(View.INVISIBLE);
            qn.setText(question[i]);
            optionA.setText(ansA[i]);
            optionB.setText(ansB[i]);
            optionC.setText(ansC[i]);
            optionD.setText(ansD[i]);
            questionnumber.setText("Q." + (i + 1));
            rvqno[i] = ("Q." + (i + 1));
            rvqn[i] = question[i];
            rvurl[i] = "http://www.frostox.com/extraclass/uploads/";
            //if(answercheck[i].equals("correct"))  rvans[i] = ("Answer: " + ans[i]+ " (Correct)");
            rvans[i] = ("Answer: " + ans[i] + " (Wrong)");
            rvexp[i] = explanation[i];

        } else if (type[i].equals("image")) {
            qn.setVisibility(View.INVISIBLE);
            imgquestion.setVisibility(View.VISIBLE);
            lltext.setVisibility(View.INVISIBLE);
            llimg.setVisibility(View.VISIBLE);
            //  lltext.setLayoutParams(empty);
            //llimg.setLayoutParams(fill);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "quest").into(imgquestion);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "A").into(imga);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "B").into(imgb);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "C").into(imgc);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "D").into(imgd);
            questionnumber.setText("Q." + (i + 1));
            rvqno[i] = ("Q." + (i + 1));
            rvqn[i] = "";
            rvurl[i] = ("http://www.frostox.com/extraclass/uploads/" + key[i] + "quest");
            //if(answercheck[i].equals("incorrect"))  rvans[i] = ("Answer: " + ans[i]+ " (Correct)");
            rvans[i] = ("Answer: " + ans[i] + " (Wrong)");
            rvexp[i] = explanation[i];
        }
        getSupportActionBar().setTitle(name[i]);
    }

    public void vibrateDevice() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Snackbar.make(viewGroup, "Wrong!", Snackbar.LENGTH_LONG).show();
    }


    public void TouchListener(View v, final String option) {

        String check = String.valueOf(v.getId());


        v.setOnTouchListener(new View.OnTouchListener() {
            public final static int FINGER_RELEASED = 0;
            public final static int FINGER_TOUCHED = 1;
            public final static int FINGER_DRAGGING = 2;
            public final static int FINGER_UNDEFINED = 3;

            private int fingerState = FINGER_RELEASED;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                        else fingerState = FINGER_UNDEFINED;

                        if (ans != null) {
                            String get = String.valueOf(view.getId());
                            Log.d("nuontouch", get);
                            if (ans[page].equals(option)) {
                                answercheck[page] = "correct";
                                onClickNext(null);
                                scorecount++;

                            } else {
                                answercheck[page] = "incorrect";
                                onClickNext((View) findViewById(R.id.dummy));
                                vibrateDevice();
                            }
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

    public void DisableTouchListener(View v, final String option) {

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    public List<ResultData> getdata() {
        List<ResultData> input = new ArrayList<>();
        String[] Qno = {"Q.1", "Q.2"};
        String[] Imgurl = {"http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png"};
        String[] Question = {"sample", "sample2"};
        String[] Answer = {"sample", "sample2"};
        String[] Explanation = {"sample", "sample2"};
        for (int i = 0; i < count; i++) {
            ResultData current = new ResultData(rvurl[i], rvqno[i], rvqn[i], rvans[i], rvexp[i]);
            current.imgqnurl = rvurl[i];
            current.qno = (rvqno[i]);
            current.qn = rvqn[i];
            current.ans = rvans[i];
            current.exp = rvexp[i];
            input.add(current);
        }
        return input;
    }

    public void setontouch(View v, final String ans) {

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TouchListener(v, ans);
            }
        });
    }
}


       /* if (i != 0) {
            if (type[i].equals("text") && type[i - 1].equals("image")) {

                setontouch(optionA, "A");
                setontouch(optionB, "B");
                setontouch(optionC, "C");
                setontouch(optionD, "D");
            } else if (type[i].equals("image") && type[i - 1].equals("text")) {

                setontouch(imga, "A");
                setontouch(imgb, "B");
                setontouch(imgc, "C");
                setontouch(imgd, "D");
            } else if (type[i].equals("image") && type[i - 1].equals("image")) {
                TouchListener(imga, "A");
                TouchListener(imgb, "B");
                TouchListener(imgc, "C");
                TouchListener(imgd, "D");
                DisableTouchListener(optionA, "A");
                DisableTouchListener(optionB, "B");
                DisableTouchListener(optionC, "C");
                DisableTouchListener(optionD, "D");
            } else if (type[i].equals("text") && type[i - 1].equals("text")) {
                TouchListener(optionA, "A");
                TouchListener(optionB, "B");
                TouchListener(optionC, "C");
                TouchListener(optionD, "D");
                DisableTouchListener(imga, "A");
                DisableTouchListener(imgb, "B");
                DisableTouchListener(imgc, "C");
                DisableTouchListener(imgd, "D");

            }
        }*/
