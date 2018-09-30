package com.github.karthyks.gitexplore.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.karthyks.gitexplore.R;

import androidx.appcompat.app.AppCompatActivity;

public class GithubAccountAuthenticator extends AbstractAccountAuthenticator {

    private final Context context;
    private final SessionManager sessionManager;
    private final Class<? extends AppCompatActivity> classLoginActivity;

    public GithubAccountAuthenticator(Context context,
                                      SessionManager sessionManager,
                                      Class<? extends AppCompatActivity> classLoginActivity) {
        super(context);
        this.context = context;
        this.sessionManager = sessionManager;
        this.classLoginActivity = classLoginActivity;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        if (sessionManager.hasActiveAccount()) {
            final Bundle bundle = new Bundle();
            bundle.putInt(AccountManager.KEY_ERROR_CODE, 1);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE,
                    context.getString(R.string.one_account_allowed));
            return bundle;
        }

        final Intent intent = new Intent(context, classLoginActivity);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                     Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                    String authTokenType, Bundle options)
            throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                              String[] features) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account)
            throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, sessionManager.onAccountRemoved(account));
        return result;
    }
}
