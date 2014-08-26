package com.leigo.android.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.FeedType;

/**
 * Created by Administrator on 2014/8/26.
 */
public class FeedTypeAdapter extends BaseAdapter {

    private boolean hasNewSecretOfFriend;

    private LayoutInflater inflater;

    public FeedTypeAdapter(Context conext) {
        inflater = LayoutInflater.from(conext);
    }

    @Override
    public int getCount() {
        return FeedType.values().length;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.feed_type_drop_down_item, parent, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(getItem(position).title());
        textView.setTag(getItem(position));
        return convertView;
    }

    @Override
    public FeedType getItem(int position) {
        return FeedType.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.feed_type_view, parent, false);
        }
        ((TextView) convertView).setText(getItem(position).title());
        return convertView;
    }

    public boolean isHasNewSecretOfFriend() {
        return this.hasNewSecretOfFriend;
    }

    public void setHasNewSecretOfFriend(boolean hasNewSecretOfFriend) {
        this.hasNewSecretOfFriend = hasNewSecretOfFriend;
        notifyDataSetChanged();
    }
}
