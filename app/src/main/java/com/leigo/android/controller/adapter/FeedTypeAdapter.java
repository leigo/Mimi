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
            convertView = inflater.inflate(R.layout.drop_down_item, parent, false);
        }
        FeedType feedType = getItem(position);
        TextView textView = (TextView) convertView;
        textView.setText(feedType.titleResId());
        if (feedType == FeedType.CIRCLE && hasNewSecretOfFriend) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_dot_small, 0);
        }
        return convertView;
    }

    public boolean isHasNewSecretOfFriend() {
        return hasNewSecretOfFriend;
    }

    public void setHasNewSecretOfFriend(boolean hasNewSecretOfFriend) {
        this.hasNewSecretOfFriend = hasNewSecretOfFriend;
        notifyDataSetChanged();
    }
}
