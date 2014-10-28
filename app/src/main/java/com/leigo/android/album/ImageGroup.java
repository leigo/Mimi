package com.leigo.android.album;

/**
 * Created by Administrator on 2014/10/28.
 */
public class ImageGroup {

    private String bucketId;
    private int count;
    private String name;
    private String thumbUri;

    public ImageGroup(String bucketId, String name) {
        this.bucketId = bucketId;
        this.name = name;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }
}
