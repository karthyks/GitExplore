package com.github.karthyks.gitexplore.account;

import android.accounts.Account;
import android.content.Context;

import com.github.karthyks.gitexplore.model.GitUser;

import androidx.annotation.NonNull;

public class AppSession extends SessionManager {
    public static final String ACCOUNT_TYPE = "com.github.karthyks.gitexplore.account";

    private static AppSession appSession;

    private AppSession(Context context) {
        super(context, ACCOUNT_TYPE);
    }

    public static AppSession get(Context context) {
        if (appSession == null) {
            synchronized (AppSession.class) {
                if (appSession == null) {
                    appSession = new AppSession(context.getApplicationContext());
                }
            }
        }
        return appSession;
    }

    @NonNull
    @Override
    protected IUser toUserModel(@NonNull Account account) {
        return GitUser.fromAccount(account, getAccountManager());
    }

    @Override
    protected void onAccountAdded(@NonNull Account account) {

    }

    @Override
    protected boolean onAccountRemoved(@NonNull Account account) {
        return true;
    }
}
