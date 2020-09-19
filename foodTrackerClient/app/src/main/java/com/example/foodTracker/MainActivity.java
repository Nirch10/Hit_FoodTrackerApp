package com.example.foodTracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/home.html");
        webView.setWebChromeClient(new WebChromeClient());


        setContentView(webView);

    }
}
