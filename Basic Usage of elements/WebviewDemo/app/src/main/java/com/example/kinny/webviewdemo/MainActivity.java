package com.example.kinny.webviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        // somtimes webviews uses the default browser instead of the native app, so the following line is compulsory
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://kinny94.github.io/portfolio/");

        // We can also render HTML data with webview.loadData("<html></html>);

    }
}
