package com.github.karthyks.gitexplore.repository;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.frameworks.CustomActivity;

public class WebActivity extends CustomActivity {

    private static final String EXTRA_URL = "extra_url";

    public static Intent getIntent(String url, Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());
        webView.loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }

    private class WebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
