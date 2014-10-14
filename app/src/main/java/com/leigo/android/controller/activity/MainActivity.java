package com.leigo.android.controller.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leigo.android.controller.adapter.FeedTypeAdapter;
import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.FeedType;
import com.leigo.android.util.Utils;


public class MainActivity extends Activity {

    private DisplayMetrics displayMetrics;

    private FeedTypeAdapter feedTypeAdapter;
    private TextView feedTypeName;
    private PopupWindow feedTypePopup;

    private FeedType lastFeedType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isAuthentication = false;
//        if (!isAuthentication) {
//            AccountGuidanceActivity.startFrom(this);
//        }
        displayMetrics = getResources().getDisplayMetrics();

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.feed_type_view, null);
        feedTypeName = (TextView) view.findViewById(R.id.feed_type_name);
        actionBar.setCustomView(view);
        actionBar.setDisplayShowCustomEnabled(true);
        feedTypeAdapter = new FeedTypeAdapter(this);
        lastFeedType = FeedType.ALL;
        feedTypeName.setText(lastFeedType.titleResId());
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(feedTypeAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                changeFeedType(FeedType.values()[itemPosition]);
                return true;
            }
        });
    }

    public void clickOnFeedType(View v) {
        if (feedTypePopup == null) {
            initFeedTypePopup();
        }
        if (feedTypePopup.isShowing()) {
            feedTypePopup.dismiss();
        }
        feedTypePopup.showAsDropDown(v, -(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.displayMetrics), -(int) TypedValue.applyDimension(1, 5.0F, displayMetrics));
    }

    private void initFeedTypePopup() {
        ListView listView = (ListView) LayoutInflater.from(this).inflate(R.layout.drop_down_list, null);
        Utils.disableOverScroll(listView);
        listView.setAdapter(feedTypeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeFeedType(FeedType.values()[position]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        feedTypePopup.dismiss();
                    }
                }, 50L);
            }
        });
        feedTypePopup = new PopupWindow(listView, displayMetrics.widthPixels / 3, -2, true);
        feedTypePopup.setBackgroundDrawable(new BitmapDrawable());
        feedTypePopup.setOutsideTouchable(true);
    }

    private void changeFeedType(FeedType feedType) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        MenuItem notificationMenuItem = menu.findItem(R.id.action_notification);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_feedback) {
            FeedbackActivity.startFrom(this);
            return true;
        }
        if (id == R.id.action_settings) {
            SettingsActivity.startFrom(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
