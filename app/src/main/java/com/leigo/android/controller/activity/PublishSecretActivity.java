package com.leigo.android.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class PublishSecretActivity extends Activity {

    private MenuItem publishMenuItem;

    private boolean hasContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_secret);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updatePublishMenuItem() {
        if(publishMenuItem.isEnabled() !=  hasContent) {
            publishMenuItem.setEnabled(hasContent);
            if(!hasContent) {
                publishMenuItem.setIcon(R.drawable.ic_send_btn_enabled);
            }else {
                publishMenuItem.setIcon(R.drawable.ic_send_btn_disabled);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish_activity_actions, menu);
        publishMenuItem = menu.findItem(R.id.action_publish_secret);
        updatePublishMenuItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
