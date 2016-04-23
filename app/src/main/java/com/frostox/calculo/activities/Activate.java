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

    EditText keyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        keyEditText = (EditText) findViewById(R.id.editText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onActivate(View view){
    }

}
