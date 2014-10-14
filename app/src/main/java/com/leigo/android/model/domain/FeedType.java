package com.leigo.android.model.domain;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/8/26.
 */
public enum FeedType {
    ALL(R.string.feed_type_all, "all"), CIRCLE(R.string.feed_type_circle, "circle"), HOT(R.string.feed_type_hot, "hot");

    private String cacheFileName;
    private boolean needRefresh;
    private int titleResId;

    FeedType(int titleResId, String cacheFileName) {
        this.titleResId = titleResId;
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

    public int titleResId() {
        return this.titleResId;
    }
}
