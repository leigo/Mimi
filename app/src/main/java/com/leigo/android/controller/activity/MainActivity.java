package com.leigo.android.controller.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.leigo.android.controller.adapter.FeedTypeAdapter;
import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.FeedType;


public class MainActivity extends Activity {

    private FeedTypeAdapter feedTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isAuthentication = false;
//        if (!isAuthentication) {
//            AccountGuidanceActivity.startFrom(this);
//        }
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setHomeButtonEnabled(true);
        feedTypeAdapter = new FeedTypeAdapter(this);
        actionBar.setListNavigationCallbacks(feedTypeAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                changeFeedType(FeedType.values()[itemPosition]);
                return true;
            }
        });
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
        if(id == R.id.action_feedback) {
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
