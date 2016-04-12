package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calculo.frostox.com.calculo.R;

public class Login extends AppCompatActivity {
    String emailtext;
    EditText email, pass;
    Firebase ref;

    //#onelasttime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://extraclass.firebaseio.com");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("jsoncheck",""+snapshot.getValue());
             // System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("onnAuth","Logged in");
              /*  Toast.makeText(Login.this, "Logged Innn", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        Log.d("onnAuthError1","User doesnt exist");
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        Log.d("onnAuthError2","Wrong Pass");
                        break;
                    default:
                        Log.d("onnAuthError3","Default");
                        break;
                }
            }
        };

        email = (EditText) findViewById(R.id.e1);
        pass = (EditText) findViewById(R.id.e2);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailtext = email.getText().toString();
                final String password = pass.getText().toString();
                if (emailtext.length() == 0) {
                    email.setError("Enter an Email");
                }
                if (!isValidEmail(emailtext) && emailtext.length() != 0) {
                    email.setError("Invalid Email");
                }

                if (password.length() == 0) {
                    pass.setError("Enter a Password");

                }
                if (password.length() > 12) {
                    pass.setError("Password should be lesser than 12 characters");

                }

                if (isValidEmail(emailtext) && (password.length() != 0 && password.length() < 13)) {
                    ref.authWithPassword("sp@gmail.com", "password", authResultHandler);
                    Log.d("onnClick", "Clickable");
                }


            }
        });


        TextView signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Intent intent = new Intent(Login.this, Signup.class);
                // startActivity(intent);
            }
        });
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint("EmailId");
        passwordWrapper.setHint("Password");
    }

    boolean isValidEmail(String emailtext) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailtext);
        return matcher.matches();
    }

   /* private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }*/
}
