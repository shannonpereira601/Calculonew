package com.frostox.calculo.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.Context;
import com.frostox.calculo.Nodes.MCQs;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by shannonpereira601 on 26/04/16.
 */
public class BGtask extends AsyncTask {

    android.content.Context context;

    public BGtask(android.content.Context context) {
        this.context = context;
    }

    private String[] folders = {"quest", "A", "B", "C", "D"};

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        for (int i = 0; i < folders.length; i++) {
            createDirIfNotExists(folders[i]);
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        for (int j = 0; j < folders.length; j++) {
            fillimages(folders[j]);
        }
        return null;
    }

    public void fillimages(final String folder) {

        Firebase ref = new Firebase("https://extraclass.firebaseio.com/mcqs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MCQs mcQs = postSnapshot.getValue(MCQs.class);
                    if (mcQs.getType().equals("image")) {
                        Picasso.with(context)
                                .load("http://www.frostox.com/extraclass/uploads/" + postSnapshot.getKey() + folder)
                                .into(getTarget(postSnapshot.getKey(), folder));
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private static Target getTarget(final String name, final String folder) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(
                                Environment.getExternalStorageDirectory() + "/ExtraClass" + "/" + folder
                                        + "/" + name + folder + ".png");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        return target;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean success = false;
        File file = new File(Environment.getExternalStorageDirectory() + "/ExtraClass", path);
        if (!file.exists()) {
            success = file.mkdirs();
        } else {
          //  Log.d("Reachedhere", "fileexists");
        }
        return success;
    }
}
