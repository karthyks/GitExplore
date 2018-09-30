package com.github.karthyks.gitexplore.account;

import android.os.Bundle;

public interface IUser {

    String getUsername();

    String getAuthToken();

    long getExpiryTime();

    Bundle toBundle();

    boolean isExpired();

    void expireSession();
}
