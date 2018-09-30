package com.github.karthyks.gitexplore.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.SplashActivity;
import com.github.karthyks.gitexplore.account.AppSession;
import com.github.karthyks.gitexplore.frameworks.CustomFragment;
import com.github.karthyks.gitexplore.model.GitUser;
import com.github.karthyks.gitexplore.transaction.FetchAuthenticatedUserTransaction;
import com.github.karthyks.gitexplore.transaction.GithubLoginTransaction;

import java.io.IOException;
import java.net.URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CALLBACK_URI;
import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CLIENT_ID;

public class GithubLoginFragment extends CustomFragment<LoginActivity> implements ILoginListener {
    private static final String TAG = GithubLoginFragment.class.getSimpleName();
    public static final String LOGIN_URL = "https://github.com/login/oauth/authorize?client_id="
            + GITHUB_CLIENT_ID + "&redirect_uri=" + GITHUB_CALLBACK_URI;

    public static GithubLoginFragment getFragment() {
        return new GithubLoginFragment();
    }

    private GithubLoginTask loginTask;
    private WebView webViewGithub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_github_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webViewGithub = view.findViewById(R.id.webview_github);
        webViewGithub.setWebViewClient(new GithubLoginClient());
        webViewGithub.getSettings().setJavaScriptEnabled(true);
        webViewGithub.loadUrl(LOGIN_URL);
    }

    @Override
    public void onSuccess(GitUser gitUser) {
        AppSession appSession = AppSession.get(getContext());
        if (appSession.getActiveAccount() != null) {
            appSession.updateSession(gitUser);
        } else {
            appSession.addUserAccount(gitUser);
        }
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.putExtra(SplashActivity.EXTRA_USER, gitUser);
        startActivity(intent);
    }

    @Override
    public void onFailure() {
        startActivity(new Intent(getContext(), SplashActivity.class));
    }


    private class GithubLoginClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            getHostingActivity().showProgressLogin(true);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            getHostingActivity().showProgressLogin(false);
            Log.d(TAG, "onPageFinished: " + url);
            URI uri = URI.create(url);
            if (uri.getQuery() != null && uri.getQuery().contains("code")) {
                String code = uri.getQuery().split("=")[1];
                Log.d(TAG, "onPageFinished: code " + code);
                if (loginTask != null) loginTask.cancel(true);
                loginTask = new GithubLoginTask();
                loginTask.setOnLoginListener(GithubLoginFragment.this);
                loginTask.execute(code);
            }
        }
    }

    private static class GithubLoginTask extends AsyncTask<String, Void, GitUser> {

        private ILoginListener loginListener;

        public void setOnLoginListener(ILoginListener loginListener) {
            this.loginListener = loginListener;
        }

        @Override
        protected GitUser doInBackground(String... strings) {
            GithubLoginTransaction transaction = new GithubLoginTransaction();

            try {
                transaction.execute(strings[0]);
                String accessToken = transaction.retrieveResult();
                Log.d(TAG, "doInBackground: " + accessToken);
                FetchAuthenticatedUserTransaction userTransaction = new FetchAuthenticatedUserTransaction(accessToken);
                userTransaction.execute();
                return userTransaction.retrieveResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GitUser gitUser) {
            if (loginListener != null) {
                if (gitUser != null) {
                    loginListener.onSuccess(gitUser);
                } else {
                    loginListener.onFailure();
                }
            }
        }
    }
}
