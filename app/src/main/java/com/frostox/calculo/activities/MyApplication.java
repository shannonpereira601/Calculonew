package com.frostox.calculo.activities;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by shannonpereira601 on 21/04/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.getDefaultConfig().setPersistenceCacheSizeBytes(100000000);
        Firebase refCourses = new Firebase("https://extraclass.firebaseio.com/courses");
        Firebase refSubjects = new Firebase("https://extraclass.firebaseio.com/subjects");
        Firebase refNotes = new Firebase("https://extraclass.firebaseio.com/notes");
        Firebase refMcqs = new Firebase("https://extraclass.firebaseio.com/mcqs");
        Firebase refUsers = new Firebase("https://extraclass.firebaseio.com/users");
        refCourses.keepSynced(true);
        refSubjects.keepSynced(true);
        refNotes.keepSynced(true);
        refMcqs.keepSynced(true);
        refUsers.keepSynced(true);

        BGtask bGtask = new BGtask(getBaseContext());
        bGtask.execute();


        //  for(int i=0;i<folders.length;i++) {

       /* for (int j = 0; j < folders.length; j++) {
            fillimages(folders[j]);
        }*/
        // }

    }



}
