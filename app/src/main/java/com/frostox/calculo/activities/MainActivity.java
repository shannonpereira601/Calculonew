package com.frostox.calculo.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.frostox.calculo.Entities.Logged;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.dao.LoggedDao;

import java.util.List;

import calculo.frostox.com.calculo.R;
import de.greenrobot.dao.query.Query;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    DaoSession daoSession;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().hide();

        imageView = (ImageView) findViewById(R.id.imageView1);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "calculo-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        LoggedDao loggedDao = daoSession.getLoggedDao();
        Query query = loggedDao.queryBuilder().build();
        List<Logged> loggedList = query.list();

        if(!loggedList.isEmpty()){
            db.close();
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else db.close();

    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void goLogin(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
