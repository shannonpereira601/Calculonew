package com.frostox.calculo.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import calculo.frostox.com.calculo.R;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://extraclass.firebaseio.com/");

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Fname.getText().toString().length() != 0 && (isEmailValid(Email.getText().toString())) && Pass.getText().toString().equals(Cpass.getText().toString())) {
                    /*ref.createUser(Email.getText().toString(), Pass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Log.d("SuccessReg", "Successfully created user account with uid: " + result.get("uid"));
                            final Firebase userRef = ref.child("users").child("shannon");
                            User user = new User(Fname.getText().toString() + " " + Lname.getText().toString(), Pass.getText().toString(),""+result.get("uid"),Email.getText().toString());
                            userRef.setValue(user);
                            Toast.makeText(Register.this, "Successfully created user account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(Register.this, "Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    });*/

                    ref.createUser(Email.getText().toString(), Pass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        Boolean status;

                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {
                            Toast.makeText(getApplicationContext(), "Registeration Successful", Toast.LENGTH_LONG).show();
                            ref.authWithPassword(Email.getText().toString(), Pass.getText().toString(), new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    User user = new User(Email.getText().toString(),  authData.getUid(), Fname.getText().toString(), status);
                                    Firebase users = ref.child("users");
                                    users.child(authData.getUid());
                                    users.push().setValue(user);
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    //changed this file
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }


    public class User {
        private String fullName;
        private String email;
        private String uid;
        private Boolean blocked;

        public User() {
        }

        public User(String email, String uid, String fullName, Boolean blocked) {
            this.fullName = fullName;
            this.uid = uid;
            this.email = email;
            this.blocked = blocked;
        }

        public String getFullName() {
            return fullName;
        }

        public String getUid() {
            return uid;
        }

        public String getEmail() {
            return email;
        }

        public Boolean getBlocked() {
            return false;
        }
    }

    public static boolean isEmailValid(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
