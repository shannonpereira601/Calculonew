package com.frostox.calculo.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.frostox.calculo.Nodes.Courses;
import com.frostox.calculo.activities.Home;
import com.frostox.calculo.pulled_sourses.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import calculo.frostox.com.calculo.R;

/**
 * Created by admin on 14/04/2016.
 */
public class RVTask extends AsyncTask<Void, Data, Void>{

    Context context;
    Activity activity1;
    RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] courses;
    Firebase ref;

    Home homeActivity;

    String json_string;
    List<Data> data = new ArrayList<>();

    String Item = "null";
    String Itemsp[];


    public RVTask(Context ctx) {
        context = ctx;
        activity1 = (Activity) ctx;
        homeActivity = (Home) activity1;
    }


    @Override
    protected void onPreExecute() {

        recyclerView = (RecyclerView)activity1.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(activity1);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        recyclerViewAdapter = new RecyclerViewAdapter(data);

        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(activity1, LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected Void doInBackground(Void... params) {
        ref = new Firebase("https://extraclass.firebaseio.com/");

        // Query query = ref.orderByChild("name").equalTo("10th");

        Firebase cref = ref.child("courses");
        Firebase sref = ref.child("subjects");

        cref.addValueEventListener(new ValueEventListener() {
            int count = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int lenghth = (int) dataSnapshot.getChildrenCount();
                courses = new String[lenghth];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Courses course = postSnapshot.getValue(Courses.class);
                    courses[count] = course.getName();
                    Data data1 = new Data(courses[count]);
                    System.out.println("onncourses " + " " + course.getName());
                    publishProgress(data1);
                    count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        return null;
    }

    @Override
    protected void onProgressUpdate(Data... values) {
        data.add(values[0]);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //progressDialog.dismiss();
    }

}
