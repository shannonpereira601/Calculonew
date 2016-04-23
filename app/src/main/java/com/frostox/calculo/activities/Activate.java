package com.frostox.calculo.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;


import calculo.frostox.com.calculo.R;

public class Activate extends AppCompatActivity{

    EditText keyEditText;
    Button act;
    TextView tv;
    String pushId;

    Firebase ref = new Firebase("https://extraclass.firebaseio.com/users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        keyEditText = (EditText) findViewById(R.id.productKey);
        act = (Button) findViewById(R.id.activate);
        tv = (TextView) findViewById(R.id.tvAcc);
        tv.setVisibility(View.INVISIBLE);

        Query query = ref.orderByChild("uid").equalTo(ref.getAuth().getUid()).limitToFirst(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    pushId = postSnapshot.getKey();
                    Log.d("Checkuid",pushId);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onActivate(View v) {
        Log.d("CheckuidonClicked",ref.getAuth().getUid());
        if (keyEditText.getText().toString().equals(ref.getAuth().getUid())) {
            Log.d("Checkuidhere","Reached");
            keyEditText.setVisibility(View.INVISIBLE);
            act.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Account Activated", Toast.LENGTH_LONG).show();

            final Firebase activeStated = ref.child(pushId);
            activeStated.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activeStated.child("activated").setValue("true");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
