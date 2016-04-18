package com.frostox.calculo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import calculo.frostox.com.calculo.R;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        WebView webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("http://square.github.io/picasso/");
    }
}
