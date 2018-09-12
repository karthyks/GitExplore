package com.github.karthyks.gitexplore.model;

import okhttp3.HttpUrl;

public class PageLink {

    public String nextPageUrl;
    public String lastPageUrl;
    public String firstPageUrl;
    public String previousPageUrl;

    public boolean hasNext = false;
    public boolean hasPrev = false;
    public int currentPageIndex = -1;


    public static PageLink fromLinkHeader(String linkHeader) {
        PageLink pageLink = new PageLink();
        if (linkHeader == null) {
            return pageLink;
        }
        String[] linkInfos = linkHeader.split(",");
        for (String linkInfo : linkInfos) {
            String[] details = linkInfo.split(";");
            String link = details[0].substring(1, details[0].length());
            String rel = details[details.length - 1];
            if (rel.contains("prev")) {
                pageLink.previousPageUrl = link;
                pageLink.currentPageIndex = Integer.parseInt(HttpUrl.parse(link) == null ? "-1"
                        : HttpUrl.parse(link).queryParameter("page")) + 1;
                pageLink.hasPrev = true;
            } else if (rel.contains("next")) {
                pageLink.nextPageUrl = link;
                pageLink.hasNext = true;
                pageLink.currentPageIndex = Integer.parseInt(HttpUrl.parse(link) == null ? "-1"
                        : HttpUrl.parse(link).queryParameter("page")) - 1;
            } else if (rel.contains("first")) {
                pageLink.firstPageUrl = link;
            } else if (rel.contains("last")) {
                pageLink.lastPageUrl = link;
            }
        }
        return pageLink;
    }
}
