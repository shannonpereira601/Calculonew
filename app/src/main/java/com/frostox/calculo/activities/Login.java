package com.frostox.calculo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calculo.frostox.com.calculo.R;

public class Login extends AppCompatActivity {
    String emailtext;
    EditText email, pass;
    Firebase ref;
    boolean check;
    SharedPreferences sharedPreferences;
    //#firebasetest


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        if(checkLogin())
        {
            Intent i = new Intent(this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://extraclass.firebaseio.com");
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
                    // ref.authWithPassword(, "password", authResultHandler);
                    setcheck();
                    ref.authWithPassword(emailtext, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                            Log.d("onnAuth", "Logged in");

                           /* Map<String, String> map = new HashMap<String, String>();
                            map.put("provider", authData.getProvider());
                            if(authData.getProviderData().containsKey("displayName")) {
                                map.put("displayName", authData.getProviderData().get("displayName").toString());
                            }
                            ref.child("users").child(authData.getUid()).setValue(map);*/

                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            final Firebase reftr = new Firebase("https://extraclass.firebaseio.com/courses");
                            reftr.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    System.out.println("onn" + snapshot.getValue());
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    System.out.println("The read failed: " + firebaseError.getMessage());
                                }
                            });

                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // there was an error
                            switch (firebaseError.getCode()) {
                                case FirebaseError.USER_DOES_NOT_EXIST:
                                    Log.d("onnAuthError1", "User doesnt exist");
                                    Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                    break;
                                case FirebaseError.INVALID_PASSWORD:
                                    Log.d("onnAuthError2", "Wrong Pass");
                                    Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Log.d("onnAuthError3", "Default");
                                    Toast.makeText(Login.this, "An error has occured. Please try again!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    Log.d("onnClick", "Clickable");
                }


            }
        });


        TextView signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint("EmailId");
        passwordWrapper.setHint("Password");
    }

    boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        check = sharedPreferences.getBoolean("check", false);
        return check;
    }

    public void setcheck() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        check = true;
        editor.putBoolean("check", check);
        editor.commit();
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
