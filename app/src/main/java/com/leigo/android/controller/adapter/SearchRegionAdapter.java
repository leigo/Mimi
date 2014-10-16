package com.leigo.android.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.Country;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2014/10/16.
 */
public class SearchRegionAdapter extends BaseAdapter {

    private List<Country> countries;
    private LayoutInflater inflater;

    public SearchRegionAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.countries = new ArrayList<Country>();
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Country getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegionListItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.region_list_item, parent, false);
            viewHolder = new RegionListItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RegionListItemViewHolder) convertView.getTag();
        }
        Country country = getItem(position);
        convertView.setTag(R.id.country_tag, country);
        viewHolder.regionView.setText(country.getName());
        viewHolder.codeView.setText(country.getCode());
        return convertView;
    }

    public class RegionListItemViewHolder {
        TextView codeView;
        TextView regionView;

        RegionListItemViewHolder(View v) {
            regionView = ((TextView) v.findViewById(R.id.region));
            codeView = ((TextView) v.findViewById(R.id.code));
        }
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }
}
