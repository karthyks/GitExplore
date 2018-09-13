package com.github.karthyks.gitexplore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Repository implements Parcelable {

    private long id;
    @SerializedName("node_id")
    private String nodeId;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    private String description;
    private String url;
    @SerializedName("html_url")
    private String httpUrl;
    private String language;
    @SerializedName("stargazers_count")
    private int stars;
    @SerializedName("watchers_count")
    private int watchers;
    @SerializedName("forks_count")
    private int forks;
    private double score;
    @SerializedName("owner")
    private Contributor contributor;

    protected Repository(Parcel in) {
        id = in.readLong();
        nodeId = in.readString();
        name = in.readString();
        fullName = in.readString();
        description = in.readString();
        url = in.readString();
        httpUrl = in.readString();
        language = in.readString();
        stars = in.readInt();
        watchers = in.readInt();
        forks = in.readInt();
        score = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nodeId);
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(httpUrl);
        dest.writeString(language);
        dest.writeInt(stars);
        dest.writeInt(watchers);
        dest.writeInt(forks);
        dest.writeDouble(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToView() {
        if (httpUrl == null) return "";
        return httpUrl.length() > 35 ? httpUrl.substring(0, 35) + "..." : httpUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getStars() {
        return stars;
    }

    public String getStarsCount() {
        return "" + stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getWatchers() {
        return watchers;
    }

    public String getWatchersCount() {
        return "" + watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public String getHttpUrl() {
        return httpUrl;
    }
}
