package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leigo.android.controller.adapter.RegionListAdapter;
import com.leigo.android.controller.adapter.SearchRegionAdapter;
import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.Bean;
import com.leigo.android.model.domain.Country;
import com.leigo.android.util.ContextToast;
import com.leigo.android.util.Utils;
import com.leigo.android.view.QuickSelectionBar;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

/**
 * Created by Administrator on 2014/8/22.
 */
public class RegionSelectionActivity extends TrackedRoboActivity {

    private List<Country> allCountries;
    private List<Country> hotCountries;

    private QuickSelectionBar quickSelectionBar;

    private PinnedHeaderListView regionListView;

    private SearchRegionAdapter searchListAdapter;

    private ListView searchListView;

    private SimpleArrayMap<Integer, String> sectionStartingIndexer;

    private void initSearchListView() {
        searchListView = (ListView) ((ViewStub) findViewById(R.id.stub)).inflate();
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("country", (Country) view.getTag(R.id.country_tag));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        searchListAdapter = new SearchRegionAdapter(this);
        searchListView.setAdapter(searchListAdapter);
    }

    public static void startFrom(Activity activity) {
        activity.startActivityForResult(new Intent(activity, RegionSelectionActivity.class), 6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        quickSelectionBar = (QuickSelectionBar) findViewById(R.id.quick_selection_bar);
        regionListView = (PinnedHeaderListView) findViewById(R.id.region_list);

        quickSelectionBar.setOnItemSelectedListener(new QuickSelectionBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (position == 0) {
                    regionListView.setSelection(0);
                } else {
                    regionListView.setSelection(position + sectionStartingIndexer.keyAt(position) + hotCountries.size());
                }

            }
        });
        regionListView.setOnItemClickListener(new PinnedHeaderListView.OnClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("country", (Country) view.getTag(R.id.country_tag));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
            }
        });
        regionListView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d("aa", (bottom > oldBottom) + "");
                if (bottom > oldBottom) {
                    Utils.setVisibility(quickSelectionBar, View.VISIBLE);
                } else {
                    Utils.setVisibility(quickSelectionBar, View.INVISIBLE);
                }
            }
        });
        loadCountriesTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.region_selection_activity_actions, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (allCountries == null) {
                    return false;
                }
                getActionBar().setIcon(R.drawable.ic_logo);
                if (searchListView == null) {
                    initSearchListView();
                }
                Utils.setVisibility(searchListView, View.VISIBLE);
                searchListAdapter.setCountries(allCountries);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Utils.setVisibility(searchListView, View.INVISIBLE);
                return true;
            }
        });
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getText(R.string.search_region));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Country> countries = new ArrayList<Country>();
                for (Country country : allCountries) {
                    if (country.getName().startsWith(newText)) {
                        countries.add(country);
                    }
                }
                searchListAdapter.setCountries(countries);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCountriesTask() {
        InputStream inputStream = getResources().openRawResource(R.raw.countries);
        try {
            String json = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            Bean bean = gson.fromJson(json, Bean.class);
            hotCountries = bean.hot_countries;
            allCountries = bean.all_countries;
            sectionStartingIndexer = new SimpleArrayMap<Integer, String>();
            sectionStartingIndexer.put(-1, "#");
            Collections.sort(allCountries);
            for (int i = 0; i < allCountries.size(); i++) {
                String identifier = allCountries.get(i).getIdentifier().substring(0, 1);
                if (!sectionStartingIndexer.containsValue(identifier)) {
                    sectionStartingIndexer.put(i, identifier);
                }
            }
            regionListView.setAdapter(new RegionListAdapter(RegionSelectionActivity.this, hotCountries, allCountries, sectionStartingIndexer));
            quickSelectionBar.setIndexer(sectionStartingIndexer);
        } catch (IOException e) {
            ContextToast.show(RegionSelectionActivity.this, R.string.toast_read_countries_data_error, Toast.LENGTH_SHORT);
            finish();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
