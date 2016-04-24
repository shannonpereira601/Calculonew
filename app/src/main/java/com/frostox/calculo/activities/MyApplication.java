package com.frostox.calculo.activities;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by shannonpereira601 on 21/04/16.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
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
    }
}
