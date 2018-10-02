package com.github.karthyks.gitexplore.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.karthyks.gitexplore.account.IUser;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GitUser implements IUser, Parcelable {
    public static final String AUTH_TOKEN = "auth_token";
    public static final String USERNAME = "user_name";
    public static final String EXPIRY_TIME = "expiry_time";
    public static final String IS_EXPIRED = "is_expired";

    private String authToken;
    @SerializedName("login")
    private String username;
    private long expiryTime;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("email")
    private String email;
    private String bio;
    private String followers;
    private String following;
    private boolean isSessionExpired = false;

    protected GitUser(Parcel in) {
        authToken = in.readString();
        username = in.readString();
        expiryTime = in.readLong();
        isSessionExpired = in.readByte() != 0;
    }

    public GitUser() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authToken);
        dest.writeString(username);
        dest.writeLong(expiryTime);
        dest.writeByte((byte) (isSessionExpired ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GitUser> CREATOR = new Creator<GitUser>() {
        @Override
        public GitUser createFromParcel(Parcel in) {
            return new GitUser(in);
        }

        @Override
        public GitUser[] newArray(int size) {
            return new GitUser[size];
        }
    };

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public long getExpiryTime() {
        return expiryTime;
    }

    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME, username);
        bundle.putString(AUTH_TOKEN, authToken);
        bundle.putString(EXPIRY_TIME, String.valueOf(expiryTime));
        return bundle;
    }

    @Override
    public boolean isExpired() {
        return new Date().after(new Date(expiryTime));
    }

    @Override
    public void expireSession() {
        isSessionExpired = true;
    }

    public boolean isSessionExpired() {
        return isSessionExpired;
    }

    public static GitUser fromAccount(Account account, AccountManager accountManager) {
        GitUser gitUser = new GitUser();
        gitUser.username = accountManager.getUserData(account, USERNAME);
        gitUser.authToken = accountManager.getUserData(account, AUTH_TOKEN);
        String expiryTime = accountManager.getUserData(account, EXPIRY_TIME);
        if (expiryTime == null || expiryTime.isEmpty()) {
            gitUser.expiryTime = 0L;
        } else {
            gitUser.expiryTime = Long.parseLong(expiryTime);
        }
        Log.d("GitUser", "fromAccount: " + gitUser.username);
        return gitUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }
}
