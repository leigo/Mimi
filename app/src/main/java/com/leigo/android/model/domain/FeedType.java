package com.leigo.android.model.domain;

/**
 * Created by Administrator on 2014/8/26.
 */
public enum FeedType {
    ALL("全部", "all"), CIRCLE("朋友", "circle"), HOT("热门", "hot");

    private String cacheFileName;
    private boolean needRefresh;
    private String title;

    FeedType(String title, String cacheFileName) {
        this.title = title;
        this.cacheFileName = cacheFileName;
    }

    public String cacheFileName() {
        return this.cacheFileName;
    }

    public boolean needRefresh() {
        return this.needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public String title() {
        return this.title;
    }
}
