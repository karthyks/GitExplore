package com.github.karthyks.gitexplore.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.util.LinkedHashSet;
import java.util.Set;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import static com.github.karthyks.gitexplore.utils.Preconditions.assertNotNull;

public abstract class SessionManager implements OnAccountsUpdateListener {
    private final Context context;
    private final String accountType;
    private AccountManager providedAccManager;
    private IUser signedInUser;
    private Set<SessionListener> sessionListeners = new LinkedHashSet<>();

    @VisibleForTesting
    SessionManager(Context context, String accountType, AccountManager providedAccManager) {
        this.context = context;
        this.accountType = accountType;
        this.providedAccManager = providedAccManager;
    }

    protected SessionManager(Context context, String accountType) {
        this(context, accountType, null);
    }

    /**
     * Creates the user account explicitly with {@link AccountManager}
     *
     * @throws IllegalStateException when used to add more than one account
     */
    public final void addUserAccount(@NonNull IUser userModel) {
        AccountManager accountManager = getAccountManager();
        Account account = new Account(userModel.getUsername(), accountType);
        if (accountManager.addAccountExplicitly(account, userModel.getAuthToken(),
                userModel.toBundle())) {
            onAccountAdded(account);
        }
        updateActiveUserCache(userModel);
    }

    /**
     * @throws NullPointerException if active user is null
     */
    public void expireUserSession() {
        IUser activeUser = getActiveUser();
        assertNotNull(activeUser, "activeUser == null");
        activeUser.expireSession();
        // notify just to send the expire event even though it happens in mutable object.
        updateSession(activeUser);
    }

    @NonNull
    @CheckResult
    protected final AccountManager getAccountManager() {
        if (providedAccManager != null)
            return providedAccManager;
        providedAccManager = AccountManager.get(context);
        return providedAccManager;
    }

    /**
     * Considers the account in the first position as the Active account, in case of multiple
     * accounts found in AccountManager.
     *
     * @return Account in the first position from {@link AccountManager}
     */
    @Nullable
    public final Account getActiveAccount() {
        AccountManager accountManager = getAccountManager();
        Account[] accounts = accountManager.getAccountsByType(accountType);
        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    /**
     * Updates the account password from {@link IUser#getAuthToken()} ()}
     * & information from {@link IUser#toBundle()}
     *
     * @throws IllegalArgumentException if account model name
     *                                  doesn't matches with {@link #getActiveAccount()}
     */
    public final void updateSession(@NonNull IUser userModel) {
        Account account = getActiveAccount();
        assertNotNull(account, "active account == null");
        if (!account.name.equals(userModel.getUsername()))
            throw new IllegalArgumentException(
                    "accountName " + userModel.getUsername() + " doesn't matches with existing active "
                            + "account " + account.name);

        AccountManager accountManager = getAccountManager();
        accountManager.setPassword(account, userModel.getAuthToken());
        Bundle userData = userModel.toBundle();
        for (String key : userData.keySet()) {
            accountManager.setUserData(account, key, userData.getString(key));
        }
        onUpdateAccount(userModel);
    }

    @SuppressWarnings("unchecked")
    @CheckResult
    @Nullable
    public final <T extends IUser> T getActiveUser() {
        if (signedInUser == null) {
            Account account = getActiveAccount();
            if (account != null)
                signedInUser = toUserModel(getActiveAccount());
        }
        return (T) signedInUser;
    }

    /**
     * @return true if no active user of session is expired
     */
    public final boolean isUserSessionExpired() {
        IUser activeUser = getActiveUser();
        return (activeUser == null || activeUser.isExpired());
    }

    /**
     * @return true if has active account and if not expired
     */
    public final boolean hasActiveAccount() {
        return (getActiveUser() != null) && (!getActiveUser().isExpired());
    }

    /**
     * Removes active account {@link #getActiveAccount()}
     */
    public final boolean removeActiveAccount(AppCompatActivity activity) {
        Account account = getActiveAccount();
        assertNotNull(account, "activeAccount == null");
        boolean status;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getAccountManager().removeAccount(account, activity, null, null);
            status = true;
        } else {
            getAccountManager().removeAccount(account, null, null);
            status = true;
        }
        updateActiveUserCache(null);
        onAccountRemoved(account);
        return status;
    }

    private void updateActiveUserCache(@Nullable IUser userModel) {
        signedInUser = userModel;
    }

    @CallSuper
    public <T extends IUser> void onUpdateAccount(T userModel) {
        updateActiveUserCache(userModel);
    }

    @NonNull
    public final String getAccountType() {
        return accountType;
    }

    /**
     * Listens to session updates via {@link AccountManager
     * #addOnAccountsUpdatedListener(OnAccountsUpdateListener, Handler, boolean)}
     * <p>
     * <b>Note:</b>
     * Perform {@link #removeSessionListener(SessionListener)} to allow garbage collection.
     */
    public final void addSessionListener(SessionListener listener) {
        sessionListeners.add(listener);
        if (sessionListeners.size() == 1)
            getAccountManager().addOnAccountsUpdatedListener(this, null, true);
    }

    public final void removeSessionListener(SessionListener listener) {
        sessionListeners.remove(listener);
        if (sessionListeners.size() == 0)
            getAccountManager().removeOnAccountsUpdatedListener(this);
    }

    @NonNull
    protected abstract IUser toUserModel(@NonNull Account account);

    /**
     * This method doesn't works on top of AccountManager. It could be used to schedule session
     * based tasks and services
     */
    protected abstract void onAccountAdded(@NonNull Account account);

    /**
     * This callback works with SessionManager instance not with AccountManager listeners.
     * It could be used to stop the session dependent tasks and services.
     *
     * @return true to allow account removal
     */
    protected abstract boolean onAccountRemoved(@NonNull Account account);

    @Override
    public final void onAccountsUpdated(Account[] accounts) {
        boolean isActive = false;
        if (accounts.length > 0) {
            for (Account account : accounts) {
                if (accountType.equals(account.type))
                    isActive = !toUserModel(account).isExpired();
            }
        }
        if (!isActive) updateActiveUserCache(null);
        for (SessionListener listener : sessionListeners) {
            listener.onSessionUpdate(isActive);
        }
    }
}
