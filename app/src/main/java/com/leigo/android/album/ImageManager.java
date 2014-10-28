package com.leigo.android.album;

import android.graphics.Bitmap;

import com.leigo.android.mimi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Administrator on 2014/10/28.
 */
public class ImageManager {

    public static DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_album_image_loading)
                .showImageForEmptyUri(R.drawable.ic_album_image_error)
                .showImageOnFail(R.drawable.ic_album_image_error)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
