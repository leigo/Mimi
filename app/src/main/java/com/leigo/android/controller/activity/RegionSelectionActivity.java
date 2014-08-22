package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leigo.android.controller.adapter.RegionListAdapter;
import com.leigo.android.controller.task.BaseAsyncTask;
import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.Bean;
import com.leigo.android.model.domain.Country;
import com.leigo.android.util.ContextToast;
import com.leigo.android.util.Utils;
import com.leigo.android.view.QuickSelectionBar;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import roboguice.inject.InjectView;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

/**
 * Created by Administrator on 2014/8/22.
 */
public class RegionSelectionActivity extends TrackedRoboActivity {

    private List<Country> allCountries;
    private List<Country> hotCountries;

    @InjectView(R.id.quick_selection_bar)
    private QuickSelectionBar quickSelectionBar;

    @InjectView(R.id.region_list)
    private PinnedHeaderListView regionListView;

    private SimpleArrayMap<Integer, String> sectionStartingIndexer;

    public static void startFrom(Activity activity) {
        activity.startActivityForResult(new Intent(activity, RegionSelectionActivity.class), 6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        quickSelectionBar.setOnItemSelectedListener(new QuickSelectionBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (position == 0) {
                    regionListView.setSelection(0);
                    return;
                }
                regionListView.setSelection(position + sectionStartingIndexer.keyAt(position) + hotCountries.size());
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
                if (bottom > oldBottom) {
                    Utils.setVisibility(quickSelectionBar, View.VISIBLE);
                } else {
                    Utils.setVisibility(quickSelectionBar, View.INVISIBLE);
                }
            }
        });
        new LoadCountriesTask(this).execute();
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

    private class LoadCountriesTask extends BaseAsyncTask<List<Country>> {

        protected LoadCountriesTask(Context context) {
            super(context);
        }

        @Override
        public List<Country> call() throws Exception {
            InputStream inputStream = null;
            try {
                inputStream = getResources().openRawResource(R.raw.countries);
                String json = IOUtils.toString(inputStream);
                Gson gson = new Gson();
                Bean bean = gson.fromJson(json, Bean.class);
                hotCountries = bean.hot_countries;
                allCountries = bean.all_countries;
                sectionStartingIndexer.put(-1, "#");
                Collections.sort(allCountries);
                for (int i = 0; i < allCountries.size(); i++) {
                    String identifier = allCountries.get(i).getIdentifier().substring(0, 1);
                    if (!sectionStartingIndexer.containsValue(identifier)) {
                        sectionStartingIndexer.put(i, identifier);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            return allCountries;
        }

        @Override
        protected void onOwnException(Exception e) throws RuntimeException {
            ContextToast.show(RegionSelectionActivity.this, R.string.toast_read_countries_data_error, Toast.LENGTH_SHORT);
            finish();
        }

        @Override
        protected void onSuccess(List<Country> countries) throws Exception {
            regionListView.setAdapter(new RegionListAdapter(RegionSelectionActivity.this, hotCountries, countries, sectionStartingIndexer));
            quickSelectionBar.setIndexer(sectionStartingIndexer);
        }
    }
}
