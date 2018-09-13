package com.github.karthyks.gitexplore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Contributor implements Parcelable {
    public static final String DEFAULT_AVATAR = "https://avatars3.githubusercontent.com/u/5435082?s=88&v=4";
    public static final String DEFAULT_LOGIN = "Anonymous";

    private long id;
    @SerializedName("node_id")
    private String nodeId;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("gravatar_id")
    private String url;
    private String login;
    private int contributions;

    protected Contributor(Parcel in) {
        id = in.readLong();
        nodeId = in.readString();
        avatarUrl = in.readString();
        url = in.readString();
        login = in.readString();
        contributions = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nodeId);
        dest.writeString(avatarUrl);
        dest.writeString(url);
        dest.writeString(login);
        dest.writeInt(contributions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contributor> CREATOR = new Creator<Contributor>() {
        @Override
        public Contributor createFromParcel(Parcel in) {
            return new Contributor(in);
        }

        @Override
        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAvatarUrl() {
        return (avatarUrl == null || avatarUrl.isEmpty()) ? DEFAULT_AVATAR : avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return (login == null || login.isEmpty()) ? DEFAULT_LOGIN : login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getContributions() {
        return contributions;
    }

    public String getContributionCount() {
        return "" + contributions;
    }
}
