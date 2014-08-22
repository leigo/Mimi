package com.leigo.android.controller.adapter;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.Country;

import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

/**
 * Created by Administrator on 2014/8/22.
 */
public class RegionListAdapter extends SectionedBaseAdapter {

    private List<Country> allCountries;
    private List<Country> hotCountries;
    private String hotRegion;
    private LayoutInflater inflater;
    private SimpleArrayMap<Integer, String> sectionStartingIndexer;

    public RegionListAdapter(Context context, List<Country> hotCountries, List<Country> allCountries, SimpleArrayMap<Integer, String> sectionStartingIndexer) {
        inflater = LayoutInflater.from(context);
        this.hotCountries = hotCountries;
        this.allCountries = allCountries;
        this.sectionStartingIndexer = sectionStartingIndexer;
        hotRegion = context.getResources().getString(R.string.hot_region);
    }

    @Override
    public Country getItem(int section, int position) {
        if (section == 0) {
            return (Country) hotCountries.get(position);
        }
        return (Country) allCountries.get(position + sectionStartingIndexer.keyAt(section));
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return sectionStartingIndexer.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (section == 0) {
            return hotCountries.size();
        }
        if (section == getSectionCount() - 1) {
            return allCountries.size() - sectionStartingIndexer.keyAt(section);
        }
        return sectionStartingIndexer.keyAt(section + 1) - sectionStartingIndexer.keyAt(section);
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        RegionListItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.region_list_item, null);
            viewHolder = new RegionListItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RegionListItemViewHolder) convertView.getTag();
        }
        Country country = getItem(section, position);
        convertView.setTag(R.id.country_tag, country);
        viewHolder.regionView.setText(country.getName());
        viewHolder.codeView.setText(country.getCode());
        return convertView;

    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pinned_header_list_view_section_header_item, parent, false);
            convertView.setTag(convertView.findViewById(R.id.header));
        }
        String str = sectionStartingIndexer.valueAt(section);
        TextView textView = (TextView) convertView.getTag();
        if (section == 0) {
            str = hotRegion;
        }
        textView.setText(str);
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
}
