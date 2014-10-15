package com.leigo.android.controller.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.leigo.android.mimi.R;

import java.util.Arrays;

/**
 * Created by Administrator on 2014/10/13.
 */
public class InviteActivity extends Activity {

    private String shareContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_invite_friend);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        shareContent = getString(R.string.invite_share_content);
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

}
