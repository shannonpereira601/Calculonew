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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frostox.calculo.Entities.McqItem;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.dao.McqItemDao;

import java.util.List;

import calculo.frostox.com.calculo.R;
import de.greenrobot.dao.query.Query;
import io.github.kexanie.library.MathView;

public class McqActivityold extends AppCompatActivity {

    MathView webView;
    MathView optionA, optionB, optionC, optionD;

    Long id;
    String name;

    private DaoSession daoSession;

    private SQLiteDatabase db;

    private List<McqItem> mcqItems;

    private int mcqItemIndex = -1;

    private McqItem mcqItem;

    private TextView textView;


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

        Intent intent = this.getIntent();
        name = intent.getStringExtra("name");
        id = intent.getLongExtra("id", 0);

        getSupportActionBar().setTitle(name);

        initDbase();
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Query query = daoSession.getMcqItemDao().queryBuilder().where(McqItemDao.Properties.McqId.eq(id)).build();

        mcqItems = query.list();

        if(mcqItems!=null && !mcqItems.isEmpty()){
            mcqItemIndex++;
            if(mcqItemIndex >= mcqItems.size()) mcqItemIndex = 0;


            mcqItem = mcqItems.get(mcqItemIndex);
        }


        load(mcqItem);

        textView.setText("Q" + (mcqItemIndex + 1));
    }

    public void initDbase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "calculo-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public void initViews(){
        /*
        webView = (MathView) findViewById(R.id.webView);
        optionA = (MathView) findViewById(R.id.optionA);
        optionB = (MathView) findViewById(R.id.optionB);
        optionC = (MathView) findViewById(R.id.optionC);
        optionD = (MathView) findViewById(R.id.optionD);
        textView = (TextView) findViewById(R.id.question);

*/
        /*webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(false);
        optionA.getSettings().setJavaScriptEnabled(true);
        optionA.getSettings().setBuiltInZoomControls(true);
        optionA.getSettings().setSupportZoom(false);
        optionB.getSettings().setJavaScriptEnabled(true);
        optionB.getSettings().setBuiltInZoomControls(true);
        optionB.getSettings().setSupportZoom(false);
        optionC.getSettings().setJavaScriptEnabled(true);
        optionC.getSettings().setBuiltInZoomControls(true);
        optionC.getSettings().setSupportZoom(false);
        optionD.getSettings().setJavaScriptEnabled(true);
        optionD.getSettings().setBuiltInZoomControls(true);
        optionD.getSettings().setSupportZoom(false);*/


        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(MathView view, String url) {
                super.onPageFinished(view, url);

                Query query = daoSession.getMcqItemDao().queryBuilder().where(McqItemDao.Properties.McqId.eq(id)).build();

                mcqItems = query.list();

                if (mcqItems != null && !mcqItems.isEmpty()) {
                    mcqItemIndex++;
                    if (mcqItemIndex >= mcqItems.size()) mcqItemIndex = 0;

                    mcqItem = mcqItems.get(mcqItemIndex);
                }
                load(mcqItem);
            }
        });*/


        /*loadDataWithBaseUrl(webView);
        loadDataWithBaseUrl(optionA);
        loadDataWithBaseUrl(optionB);
        loadDataWithBaseUrl(optionC);
        loadDataWithBaseUrl(optionD);*/

        optionA.setOnTouchListener(new View.OnTouchListener() {
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

                        if (mcqItem != null) {
                            if (mcqItem.getAnswer().equals("A")) {
                                onClickNext(null);
                            } else vibrateDevice();
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



        optionB.setOnTouchListener(new View.OnTouchListener() {
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

                        if (mcqItem != null) {
                            if (mcqItem.getAnswer().equals("B")) {
                                onClickNext(null);
                            } else vibrateDevice();
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

        optionC.setOnTouchListener(new View.OnTouchListener() {
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

                        if (mcqItem != null) {
                            if (mcqItem.getAnswer().equals("C")) {
                                onClickNext(null);
                            } else vibrateDevice();
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

        optionD.setOnTouchListener(new View.OnTouchListener() {
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

                        if (mcqItem != null) {
                            if (mcqItem.getAnswer().equals("D")) {
                                onClickNext(null);
                            } else vibrateDevice();
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

    public void onClickPrev(View v){



        if(mcqItems!=null&&!mcqItems.isEmpty()){
            mcqItemIndex--;
            if(mcqItemIndex < 0) mcqItemIndex = mcqItems.size()-1;

            mcqItem = mcqItems.get(mcqItemIndex);
        }

        load(mcqItem);

        textView.setText("Q"+(mcqItemIndex+1));

    }

    public void onClickNext(View v){

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        if(v==null)
            Snackbar.make(viewGroup, "Correct!", Snackbar.LENGTH_LONG).show();

        if(mcqItems!=null && !mcqItems.isEmpty()){
            mcqItemIndex++;
            if(mcqItemIndex >= mcqItems.size()) mcqItemIndex = 0;


            mcqItem = mcqItems.get(mcqItemIndex);
        }


        load(mcqItem);

        textView.setText("Q"+(mcqItemIndex+1));
    }


    public void load(McqItem mcqItem){

        if(mcqItem != null) {

            webView.loadUrl("javascript:document.getElementById('math').innerHTML=<font color=\"#000000\">`" + mcqItem.getQuestion() + "`</font>;", null);
            optionA.loadUrl("javascript:document.getElementById('math').innerHTML=<font color=\"#000000\">`" + mcqItem.getOptionA() + "`</font>;", null);
            optionB.loadUrl("javascript:document.getElementById('math').innerHTML=<font color=\"#000000\">`" + mcqItem.getOptionB() + "`</font>;", null);
            optionC.loadUrl("javascript:document.getElementById('math').innerHTML=<font color=\"#000000\">`" + mcqItem.getOptionC() + "`</font>;", null);
            optionD.loadUrl("javascript:document.getElementById('math').innerHTML=<font color=\"#000000\">`" + mcqItem.getOptionD() + "`</font>;", null);

            webView.setText(mcqItem.getQuestion());
            optionA.setText(mcqItem.getOptionA());
            optionB.setText(mcqItem.getOptionB());
            optionC.setText(mcqItem.getOptionC());
            optionD.setText(mcqItem.getOptionD());



        }
    }

    public void vibrateDevice(){
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Snackbar.make(viewGroup, "Wrong!", Snackbar.LENGTH_LONG).show();
    }


}
