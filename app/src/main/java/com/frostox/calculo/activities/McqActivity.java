package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.frostox.calculo.Entities.McqItem;
import com.frostox.calculo.Nodes.MCQs;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.dao.McqItemDao;
import com.squareup.picasso.Picasso;

import java.util.List;

import calculo.frostox.com.calculo.R;
import io.github.kexanie.library.MathView;

public class McqActivity extends AppCompatActivity {


    TextView optionA, optionB, optionC, optionD, qn, questionnumber;
    ImageView imga, imgb, imgc, imgd, imgquestion;

    String id, namebar, difficulty;

    String[] ans, ansA, ansB, ansC, ansD, explanation, explanationType, name, question, topic, type, key;

    int count;
    int page;

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
        Firebase.setAndroidContext(this);
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
                key = new String[length];
                count = 0;
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
                        count++;
                    }
                }
                if (count != 0) {
                    initViews();
                } else {
                    Toast.makeText(McqActivity.this, "Sorry there are no MCQs", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        getSupportActionBar().setTitle(name[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initViews() {
        if (question != null) {
            qn = (TextView) findViewById(R.id.question);
            optionA = (TextView) findViewById(R.id.ansa);
            optionB = (TextView) findViewById(R.id.ansb);
            optionC = (TextView) findViewById(R.id.ansc);
            optionD = (TextView) findViewById(R.id.ansd);
            questionnumber = (TextView) findViewById(R.id.questionnumber);
            imga = (ImageView) findViewById(R.id.imga);
            imgb = (ImageView) findViewById(R.id.imgb);
            imgc = (ImageView) findViewById(R.id.imgc);
            imgd = (ImageView) findViewById(R.id.imgd);
            imgquestion = (ImageView) findViewById(R.id.imgquestion);
            page = 0;
            load(0);
        }

        TouchListener(optionA, "A");
        TouchListener(optionB, "B");
        TouchListener(optionC, "C");
        TouchListener(optionD, "D");
        TouchListener(imga, "A");
        TouchListener(imgb, "B");
        TouchListener(imgc, "C");
        TouchListener(imgd, "D");

    }

    public void onClickPrev(View v) {
        --page;
        if (page != (-1)) {
            load(page);
        } else {
            Toast.makeText(McqActivity.this, "No MCQs Behind", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickNext(View v) {
        ++page;
        Log.d("nuonclicknext", "" + page + " " + count + " " + key[page]);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        if (v == null)
            Snackbar.make(viewGroup, "Correct!", Snackbar.LENGTH_LONG).show();

        if (page != count || count!=0) {
            load(page);
        } else {
            Toast.makeText(McqActivity.this, "No More MCQs", Toast.LENGTH_LONG).show();
        }
    }

    public void load(int i) {
        if (type[i].equals("text")) {
            textVisible();
            imgInvisible();

            qn.setText(question[i]);
            optionA.setText(ansA[i]);
            optionB.setText(ansB[i]);
            optionC.setText(ansC[i]);
            optionD.setText(ansD[i]);

        }
        if (type[i].equals("image")) {
            textInvisible();
            imgVisible();
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "quest").into(imgquestion);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "A").into(imga);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "B").into(imgb);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "C").into(imgc);
            Picasso.with(getBaseContext()).load("http://www.frostox.com/extraclass/uploads/" + key[i] + "D").into(imgd);
        }
        questionnumber.setText("Q." + (i + 1));
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
                            if (ans[0].equals(option)) {
                                onClickNext(null);
                            } else {
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

    public void textVisible() {
        qn.setVisibility(View.VISIBLE);
        optionA.setVisibility(View.VISIBLE);
        optionB.setVisibility(View.VISIBLE);
        optionC.setVisibility(View.VISIBLE);
        optionD.setVisibility(View.VISIBLE);
    }

    public void textInvisible() {
        qn.setVisibility(View.INVISIBLE);
        optionA.setVisibility(View.INVISIBLE);
        optionB.setVisibility(View.INVISIBLE);
        optionC.setVisibility(View.INVISIBLE);
        optionD.setVisibility(View.INVISIBLE);
    }

    public void imgVisible() {
        imgquestion.setVisibility(View.VISIBLE);
        imga.setVisibility(View.VISIBLE);
        imgb.setVisibility(View.VISIBLE);
        imgc.setVisibility(View.VISIBLE);
        imgd.setVisibility(View.VISIBLE);
    }

    public void imgInvisible() {
        imgquestion.setVisibility(View.INVISIBLE);
        imga.setVisibility(View.INVISIBLE);
        imgb.setVisibility(View.INVISIBLE);
        imgc.setVisibility(View.INVISIBLE);
        imgd.setVisibility(View.INVISIBLE);
    }
}
