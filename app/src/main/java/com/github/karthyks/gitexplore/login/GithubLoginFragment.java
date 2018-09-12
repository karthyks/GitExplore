package com.github.karthyks.gitexplore.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.SplashActivity;
import com.github.karthyks.gitexplore.frameworks.CustomFragment;
import com.github.karthyks.gitexplore.transaction.GithubLoginTransaction;

import java.io.IOException;
import java.net.URI;

import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CALLBACK_URI;
import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CLIENT_ID;

public class GithubLoginFragment extends CustomFragment<LoginActivity> implements ILoginListener {

    private static final String TAG = GithubLoginFragment.class.getSimpleName();

    public static GithubLoginFragment getFragment() {
        return new GithubLoginFragment();
    }

    private GithubLoginTask loginTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_github_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webViewGithub = view.findViewById(R.id.webview_github);
        webViewGithub.setWebViewClient(new GithubLoginClient());
        webViewGithub.getSettings().setJavaScriptEnabled(true);
        webViewGithub.loadUrl("https://github.com/login/oauth/authorize?client_id="
                + GITHUB_CLIENT_ID + "&redirect_uri=" + GITHUB_CALLBACK_URI);
    }

    @Override
    public void onSuccess(String accessToken) {
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.putExtra(SplashActivity.ACCESS_TOKEN, accessToken);
        startActivity(intent);
    }

    @Override
    public void onFailure() {

    }


    private class GithubLoginClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: " + url);
            URI uri = URI.create(url);
            if (uri.getQuery().contains("code")) {
                String code = uri.getQuery().split("=")[1];
                Log.d(TAG, "onPageFinished: code " + code);
                if (loginTask != null) loginTask.cancel(true);
                loginTask = new GithubLoginTask();
                loginTask.setOnLoginListener(GithubLoginFragment.this);
                loginTask.execute(code);
            }
        }
    }

    private static class GithubLoginTask extends AsyncTask<String, Void, String> {

        private ILoginListener loginListener;

        public void setOnLoginListener(ILoginListener loginListener) {
            this.loginListener = loginListener;
        }

        @Override
        protected String doInBackground(String... strings) {
            GithubLoginTransaction transaction = new GithubLoginTransaction();
            try {
                transaction.execute(strings[0]);
                return transaction.retrieveResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (loginListener != null) {
                if (s != null && !s.isEmpty()) {
                    loginListener.onSuccess(s);
                } else {
                    loginListener.onFailure();
                }
            }
        }
    }
}
