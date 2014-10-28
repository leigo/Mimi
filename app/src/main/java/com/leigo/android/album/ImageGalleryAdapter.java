package com.leigo.android.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.leigo.android.mimi.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/28.
 */
public class ImageGalleryAdapter extends BaseAdapter {

    private int imageSize;
    private List<String> imageUris;
    private LayoutInflater inflater;

    public ImageGalleryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.imageUris = new ArrayList<String>();
        this.imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    public void clear() {
        imageUris.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public String getItem(int position) {
        return imageUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_gallery_item, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(imageSize, imageSize));
        }
        ImageView imageView = (ImageView) convertView;
        ImageLoader.getInstance().displayImage(imageUris.get(position), imageView, ImageManager.getDisplayImageOptions(), null);
        return convertView;
    }

    public void setImagePaths(List<String> imageUris) {
        this.imageUris = imageUris;
        notifyDataSetChanged();
    }
}
