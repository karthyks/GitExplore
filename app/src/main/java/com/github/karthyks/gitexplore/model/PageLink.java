package com.github.karthyks.gitexplore.model;

import android.util.Log;

import okhttp3.HttpUrl;

public class PageLink {

    private static final String TAG = PageLink.class.getSimpleName();
    public String nextPageUrl;
    public String lastPageUrl;
    public String firstPageUrl;
    public String previousPageUrl;

    public boolean hasNext = false;
    public boolean hasPrev = false;
    public int currentPageIndex = 1;


    public static PageLink fromLinkHeader(String linkHeader) {
        PageLink pageLink = new PageLink();
        if (linkHeader == null) {
            return pageLink;
        }
        String[] linkInfos = linkHeader.split(",");
        for (String linkInfo : linkInfos) {
            String[] details = linkInfo.split(";");
            String link = details[0].substring(details[0].indexOf('<') + 1, details[0].indexOf('>'));
            Log.d(TAG, "fromLinkHeader: " + link);
            String rel = details[details.length - 1];
            Log.d(TAG, "fromLinkHeader: " + rel);
            Log.d(TAG, "fromLinkHeader: " + HttpUrl.parse(link));
            if (rel.contains("prev")) {
                pageLink.previousPageUrl = link;
                pageLink.currentPageIndex = Integer.parseInt(HttpUrl.parse(link) == null ? "1"
                        : HttpUrl.parse(link).queryParameter("page")) + 1;
                pageLink.hasPrev = true;
            }
            if (rel.contains("next")) {
                pageLink.nextPageUrl = link;
                pageLink.hasNext = true;
                pageLink.currentPageIndex = Integer.parseInt(HttpUrl.parse(link) == null ? "1"
                        : HttpUrl.parse(link).queryParameter("page")) - 1;
            }
            if (rel.contains("first")) {
                pageLink.firstPageUrl = link;
            }
            if (rel.contains("last")) {
                pageLink.lastPageUrl = link;
            }
        }
        return pageLink;
    }
}
