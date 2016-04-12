package com.frostox.calculo.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frostox.calculo.Entities.Logged;
import com.frostox.calculo.dao.DaoMaster;
import com.frostox.calculo.dao.DaoSession;
import com.frostox.calculo.Entities.User;
import com.frostox.calculo.dao.LoggedDao;
import com.frostox.calculo.dao.UserDao;

import java.util.List;

import calculo.frostox.com.calculo.R;
import de.greenrobot.dao.query.Query;

public class Activate extends AppCompatActivity {
//iiugigi
    DaoSession daoSession;

    EditText keyEditText;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        keyEditText = (EditText) findViewById(R.id.editText);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "calculo-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        insertInitValue(daoSession);


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

    public void onActivate(View view){
        UserDao userDao = daoSession.getUserDao();

        Query query = userDao.queryBuilder().where(UserDao.Properties.Key.eq(keyEditText.getText())).build();
        List<User> users = query.list();

        if(users.isEmpty())
            Toast.makeText(this, "Not Found", Toast.LENGTH_LONG).show();
        else {

            Logged logged = new Logged();
            logged.setUser(users.get(0));

            LoggedDao loggedDao = daoSession.getLoggedDao();
            loggedDao.insertOrReplace(logged);

            db.close();

            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    public void insertInitValue(DaoSession daoSession){


        UserDao userDao = daoSession.getUserDao();

        Query query = userDao.queryBuilder().where(UserDao.Properties.Name.eq(keyEditText.getText())).build();
        List<User> users = query.list();

        if(users.isEmpty()) {

            User user = new User();
            user.setName("sandeep");
            user.setKey("sandeep");

            userDao.insertOrReplace(user);


        }
    }

}
