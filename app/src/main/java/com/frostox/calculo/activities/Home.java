package com.frostox.calculo.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.frostox.calculo.Entities.Mcq;
import com.frostox.calculo.Entities.McqItem;
import com.frostox.calculo.Entities.Note;
import com.frostox.calculo.Entities.Standard;
import com.frostox.calculo.Entities.Subject;
import com.frostox.calculo.Entities.Topic;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.dao.McqDao;
import com.frostox.calculo.dao.NoteDao;
import com.frostox.calculo.dao.StandardDao;
import com.frostox.calculo.dao.SubjectDao;
import com.frostox.calculo.dao.TopicDao;
import com.frostox.calculo.enums.Entities;
import com.frostox.calculo.fragments.EntityFragment;

import java.util.List;

import calculo.frostox.com.calculo.R;
import de.greenrobot.dao.query.Query;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EntityFragment.OnFragmentInteractionListener {

    CoordinatorLayout coordinatorLayout;


    private DaoSession daoSession;

    private SQLiteDatabase db;

    private DrawerLayout drawer;

    private EntityFragment<Standard, StandardDao> standardFragment;

    private EntityFragment<Subject, SubjectDao> subjectFragment;

    private EntityFragment<Topic, TopicDao> topicFragment;

    private EntityFragment<Mcq, McqDao> mcqFragment;

    private EntityFragment<Note, NoteDao> noteFragment;

    private boolean mcqMode = true;

    private Entities currentList = Entities.STANDARD;

    private TextView courses, subjects, topics, mcqnotes;

    private HorizontalScrollView scrollView;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.colayout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "calculo-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        addDummyData();

        standardFragment = new EntityFragment<>();
        standardFragment.setDao(daoSession.getStandardDao());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, standardFragment)
                .commit();

        courses = (TextView) findViewById(R.id.courses);
        subjects = (TextView) findViewById(R.id.subjects);
        topics = (TextView) findViewById(R.id.topics);
        mcqnotes = (TextView) findViewById(R.id.mcqnotes);

        drawer.openDrawer(GravityCompat.START);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        if(db!=null&&db.isOpen())
            db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(db==null || !db.isOpen()){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "calculo-db", null);
            db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }
    */
    public void addDummyData() {

        Query query = daoSession.getStandardDao().queryBuilder().limit(1).build();
        List<Standard> standards = query.list();

        if (!standards.isEmpty()) return;

        Standard standard = new Standard();
        standard.setName("9th Grade");

        daoSession.getStandardDao().insertOrReplace(standard);

        Standard standard2 = new Standard();
        standard2.setName("10th Grade");

        daoSession.getStandardDao().insertOrReplace(standard2);

        Standard standard3 = new Standard();
        standard3.setName("CET");

        daoSession.getStandardDao().insertOrReplace(standard3);

        Subject subject = new Subject();
        subject.setName("History");
        subject.setStandard(standard);

        daoSession.getSubjectDao().insertOrReplace(subject);


        Subject subject2 = new Subject();
        subject2.setName("Geography");
        subject2.setStandard(standard);

        daoSession.getSubjectDao().insertOrReplace(subject2);

        Subject subject3 = new Subject();
        subject3.setName("Maths 1");
        subject3.setStandard(standard);

        daoSession.getSubjectDao().insertOrReplace(subject3);

        subject = new Subject();
        subject.setName("History");
        subject.setStandard(standard2);


        daoSession.getSubjectDao().insertOrReplace(subject);


        subject2 = new Subject();
        subject2.setName("Geography");
        subject2.setStandard(standard2);


        daoSession.getSubjectDao().insertOrReplace(subject2);

        subject3 = new Subject();
        subject3.setName("Maths 1");
        subject3.setStandard(standard2);

        daoSession.getSubjectDao().insertOrReplace(subject3);

        subject = new Subject();
        subject.setName("Calculus");
        subject.setStandard(standard3);


        daoSession.getSubjectDao().insertOrReplace(subject);

        subject2 = new Subject();
        subject2.setName("Algebra");
        subject2.setStandard(standard3);


        daoSession.getSubjectDao().insertOrReplace(subject2);

        subject3 = new Subject();
        subject3.setName("Biology");
        subject3.setStandard(standard3);


        daoSession.getSubjectDao().insertOrReplace(subject3);

        Topic integrals = new Topic();
        integrals.setName("Integrals");
        integrals.setSubject(subject);

        daoSession.getTopicDao().insertOrReplace(integrals);

        Topic matrices = new Topic();
        matrices.setName("Matrices");
        matrices.setSubject(subject2);

        daoSession.getTopicDao().insertOrReplace(matrices);

        Topic bioChemOfCells = new Topic();
        bioChemOfCells.setName("Cells");
        bioChemOfCells.setSubject(subject3);

        daoSession.getTopicDao().insertOrReplace(bioChemOfCells);

        Mcq mcq = new Mcq();
        mcq.setName("MCQ 1");
        mcq.setTopic(integrals);

        daoSession.getMcqDao().insertOrReplace(mcq);

        McqItem mcqItem = new McqItem();
        mcqItem.setQuestion("$$\\int_{0}^{1} {e^{2 \\hspace{1mm} in \\hspace{1mm} x} dx}$$");
        mcqItem.setOptionA("$$0$$");
        mcqItem.setOptionB("$$\\frac{1}{2}$$");
        mcqItem.setOptionC("$$\\frac{1}{3}$$");
        mcqItem.setOptionD("$$\\frac{1}{4}$$");
        mcqItem.setAnswer("A");
        mcqItem.setMcq(mcq);

        daoSession.getMcqItemDao().insertOrReplace(mcqItem);

        mcqItem = new McqItem();
        mcqItem.setQuestion("$$\\int_{0}^{\\frac{\\pi}{2}} {tan^2 \\hspace{1mm} x \\hspace{1mm} dx}$$");
        mcqItem.setOptionA("$$1-\\frac{\\pi}{4}$$");
        mcqItem.setOptionB("$$1+\\frac{\\pi}{4}$$");
        mcqItem.setOptionC("$$\\frac{\\pi}{4}-1$$");
        mcqItem.setOptionD("$$\\frac{\\pi}{4}$$");
        mcqItem.setAnswer("B");
        mcqItem.setMcq(mcq);

        daoSession.getMcqItemDao().insertOrReplace(mcqItem);


        mcqItem = new McqItem();
        mcqItem.setQuestion("$$\\int_{0}^{\\frac{\\pi}{2}} {\\frac{x \\hspace{1mm} + \\hspace{1mm} sin \\hspace{1mm} x}{1 \\hspace{1mm} + \\hspace{1mm} cos \\hspace{1mm} x} dx}$$");
        mcqItem.setOptionA("$$-log \\hspace{1mm} 2$$");
        mcqItem.setOptionB("$$-log \\hspace{1mm} 2$$");
        mcqItem.setOptionC("$$\\frac{\\pi}{2}$$");
        mcqItem.setOptionD("$$0$$");
        mcqItem.setAnswer("C");
        mcqItem.setMcq(mcq);

        daoSession.getMcqItemDao().insertOrReplace(mcqItem);


        Mcq mcq2 = new Mcq();
        mcq2.setName("MCQ 1");
        mcq2.setTopic(matrices);

        daoSession.getMcqDao().insertOrReplace(mcq2);

        McqItem mcqItem1 = new McqItem();
        mcqItem1.setQuestion("$$2\\begin{bmatrix} " +
                "  1 & 3\\\\" +
                "  2 & 3\\\\" +
                "  4 & 6" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionA("$$\\begin{bmatrix} " +
                "  2 & 16\\\\" +
                "  4 & 6\\\\" +
                "  8 & 12" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionB("$$\\begin{bmatrix} " +
                "  2 & 6\\\\" +
                "  4 & 6\\\\" +
                "  8 & 12" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionC("$$\\begin{bmatrix} " +
                "  2 & 6\\\\" +
                "  4 & 6\\\\" +
                "  8 & 2" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionD("$$\\begin{bmatrix} " +
                "  2 & 6\\\\" +
                "  1 & 6\\\\" +
                "  8 & 12" +
                "\\end{bmatrix}$$");

        mcqItem1.setAnswer("B");
        mcqItem1.setMcq(mcq2);

        daoSession.getMcqItemDao().insertOrReplace(mcqItem1);

        mcqItem1 = new McqItem();
        mcqItem1.setQuestion("$$3\\begin{bmatrix} " +
                "  1 & 1\\\\" +
                "  1 & 1\\\\" +
                "  3 & 3" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionA("$$\\begin{bmatrix} " +
                "  2 & 16\\\\" +
                "  1 & 1\\\\" +
                "  1 & 1" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionB("$$\\begin{bmatrix} " +
                "  1 & 1\\\\" +
                "  1 & 1\\\\" +
                "  1 & 1" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionC("$$\\begin{bmatrix} " +
                "  2 & 6\\\\" +
                "  4 & 6\\\\" +
                "  8 & 2" +
                "\\end{bmatrix}$$");
        mcqItem1.setOptionD("$$\\begin{bmatrix} " +
                "  3 & 3\\\\" +
                "  3 & 3\\\\" +
                "  9 & 9" +
                "\\end{bmatrix}$$");

        mcqItem1.setAnswer("D");
        mcqItem1.setMcq(mcq2);

        daoSession.getMcqItemDao().insertOrReplace(mcqItem1);


        Note note = new Note();
        note.setName("BioChemestry of Cells");
        note.setTopic(bioChemOfCells);
        note.setFile("");

        daoSession.getNoteDao().insertOrReplace(note);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentList != Entities.STANDARD) {
            Log.d("Check", "onnBckClld");
            navPrev();
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mcq) {
            mcqMode = true;
            if (currentList == Entities.MCQ)
                navPrev();

            //TODO Fragment operations
        } else if (id == R.id.nav_notes) {
            mcqMode = false;
            if (currentList == Entities.MCQ)
                navPrev();
            //TODO Fragement operations
        } else if (id == R.id.nav_share) {
            Snackbar.make(coordinatorLayout, "Not Implemented Yet", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Snackbar.make(coordinatorLayout, "Not Implemented Yet", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_about_us) {
            Snackbar.make(coordinatorLayout, "Not Implemented Yet", Snackbar.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onFragmentInteraction(Uri uri) {
        Snackbar.make(coordinatorLayout, "fragment interaction", Snackbar.LENGTH_LONG).show();
    }


    public void navNext(Long id, String name) {
        name = name.toUpperCase();
        switch (currentList) {
            case STANDARD:
                if(name.equals("9TH GRADE"))
                {
                    name= "9th GRADE";
                }
                else if(name.equals("10TH GRADE"))
                {
                    name= "10th GRADE";
                }
                currentList = Entities.SUBJECT;
                subjectFragment = new EntityFragment<>();
                subjectFragment.setDao(daoSession.getSubjectDao());

                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("columnName", "standardId");
                // set Fragmentclass Arguments
                subjectFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, subjectFragment).addToBackStack("Standard")
                        .commit();

                courses.setText(name + " >");
                subjects.setVisibility(View.VISIBLE);
                subjects.setText("SUBJECTS >");
                topics.setVisibility(View.GONE);
                mcqnotes.setVisibility(View.GONE);

                break;
            case SUBJECT:
                currentList = Entities.TOPIC;
                topicFragment = new EntityFragment<>();
                topicFragment.setDao(daoSession.getTopicDao());

                bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("columnName", "subjectId");
                // set Fragmentclass Arguments
                topicFragment.setArguments(bundle);

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, topicFragment).addToBackStack("Subject")
                        .commit();

                subjects.setText(name + " >");
                topics.setVisibility(View.VISIBLE);
                mcqnotes.setVisibility(View.GONE);

                break;
            case TOPIC:
                if (mcqMode) {
                    currentList = Entities.MCQ;
                    mcqFragment = new EntityFragment<>();
                    mcqFragment.setDao(daoSession.getMcqDao());

                    bundle = new Bundle();
                    bundle.putLong("id", id);
                    bundle.putString("columnName", "topicId");
                    // set Fragmentclass Arguments
                    mcqFragment.setArguments(bundle);

                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, mcqFragment).addToBackStack("Topic")
                            .commit();

                    topics.setText(name + " >");
                    mcqnotes.setVisibility(View.VISIBLE);
                    mcqnotes.setText("MCQs >");
                } else {
                    currentList = Entities.NOTE;
                    noteFragment = new EntityFragment<>();
                    noteFragment.setDao(daoSession.getNoteDao());

                    bundle = new Bundle();
                    bundle.putLong("id", id);
                    bundle.putString("columnName", "topicId");
                    // set Fragmentclass Arguments
                    noteFragment.setArguments(bundle);

                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, noteFragment).addToBackStack("Topic")
                            .commit();

                    topics.setText(name + " >");
                    mcqnotes.setVisibility(View.VISIBLE);
                    mcqnotes.setText("NOTES >");
                }


                break;
            case MCQ:
                //Goto mcq activity
                Intent intent = new Intent(this, McqActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);

                startActivity(intent);
                break;
            case NOTE:
                //Goto note activity
                intent = new Intent(this, ScreenSlideActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
        }


        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    public void navPrev() {


        switch (currentList) {

            case SUBJECT:
                currentList = Entities.STANDARD;
                subjects.setVisibility(View.GONE);
                courses.setText("COURSES >");
                break;

            case TOPIC:
                currentList = Entities.SUBJECT;
                topics.setVisibility(View.GONE);
                subjects.setText("SUBJECTS >");
                break;

            case MCQ:
                currentList = Entities.TOPIC;
                mcqnotes.setVisibility(View.GONE);
                topics.setText("TOPICS >");
                break;

            case NOTE:
                currentList = Entities.TOPIC;
                mcqnotes.setVisibility(View.GONE);
                topics.setText("TOPICS >");

        }

        getSupportFragmentManager().popBackStack();

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    public void navRoot() {

    }
}
