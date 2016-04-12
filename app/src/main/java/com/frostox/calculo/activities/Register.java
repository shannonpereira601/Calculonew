package com.frostox.calculo.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import calculo.frostox.com.calculo.R;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText Fname, Lname, Email, Pass, Cpass;

        Fname = (EditText) findViewById(R.id.Fname);
        Lname = (EditText) findViewById(R.id.Lname);
        Email = (EditText) findViewById(R.id.email);
        Pass = (EditText) findViewById(R.id.regPass);
        Cpass = (EditText) findViewById(R.id.confPass);


        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Fname.getText().toString().length() == 0) {
                        Fname.setError("First Name Required!");
                    }
                    if (!(isEmailValid(Email.getText().toString()))) {
                        Email.setError("Email Not Valid");
                    }
                    if (Pass.getText().toString().length() == 0) {
                        Pass.setError("Password Required!");
                    }
                    if (Cpass.getText().toString().length() == 0) {
                        Cpass.setError("Confirm Password!");
                    }
                    if (!Cpass.getText().toString().equals(Pass.getText().toString())) {
                        Cpass.setError("Password Doesn't Match!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if (Fname.getText().toString().length()!=0 && (isEmailValid(Email.getText().toString())) && Pass.getText().toString().equals(Cpass.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Successful Regis",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean isEmailValid(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
