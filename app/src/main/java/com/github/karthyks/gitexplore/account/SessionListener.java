package com.github.karthyks.gitexplore.account;

public interface SessionListener {

    /**
     * Called by {@link SessionManager} when there is
     * a change in Account information.
     * <p>
     * Use this callback to check account state if still active
     * or if any account information got updated
     *
     * @param isActive has false if session is expired or removed
     */
    void onSessionUpdate(boolean isActive);
}
