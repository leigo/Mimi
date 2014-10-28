package com.leigo.android.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/28.
 */
public class GalleryPickerAdapter extends BaseAdapter {

    private List<ImageGroup> imageGroups;
    private LayoutInflater inflater;

    public GalleryPickerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.imageGroups = new ArrayList<ImageGroup>();
    }

    public void addGroup(ImageGroup imageGroup) {
        imageGroups.add(imageGroup);
        notifyDataSetChanged();
    }

    public void clear() {
        imageGroups.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageGroups.size();
    }

    @Override
    public ImageGroup getItem(int position) {
        return imageGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_picker_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageGroup imageGroup = imageGroups.get(position);
        viewHolder.titleView.setText(imageGroup.getName() + "(" + imageGroup.getCount() + ")");
        ImageLoader.getInstance().displayImage(imageGroup.getThumbUri(), viewHolder.imageView, ImageManager.getDisplayImageOptions(), null);
        return convertView;
    }


    private static class ViewHolder {
        private ImageView imageView;
        private TextView titleView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
        }
    }
}
